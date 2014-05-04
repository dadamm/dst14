package dst.ass2.ejb.session;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass2.ejb.dto.AuditLogDTO;
import dst.ass2.ejb.dto.BillDTO;
import dst.ass2.ejb.dto.BillDTO.BillPerTask;
import dst.ass2.ejb.management.PriceManagementBean;
import dst.ass2.ejb.management.interfaces.IPriceManagementBean;
import dst.ass2.ejb.model.IAuditLog;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.interfaces.IGeneralManagementBean;
import dst.ass2.ejb.util.EJBUtils;

@Stateless
@Remote(IGeneralManagementBean.class)
public class GeneralManagementBean implements IGeneralManagementBean {
	
	@PersistenceContext private EntityManager entityManager;
	private IPriceManagementBean priceManagementBean;
	
	public GeneralManagementBean() throws NamingException {
		priceManagementBean = EJBUtils.lookup(new InitialContext(), PriceManagementBean.class);
	}

	@Override
	public void addPrice(Integer nrOfHistoricalTasks, BigDecimal price) {
		priceManagementBean.setPrice(nrOfHistoricalTasks, price);
	}
	
	private IUser getUser(String username) throws NoResultException {
		String sql = "select u from User u where username = :username";
		Query query = entityManager.createQuery(sql);
		query.setParameter("username", username);
		IUser user = (IUser) query.getSingleResult();
		return user;
	}
	
	private List<ITask> getUnpaidTasks(String username) {
		String sql = "select t from User u "
				+ "join u.tasks t "
				+ "join t.taskProcessing tp "
				+ "where t.isPaid = false and tp.status = 'FINISHED' and u.username = :username";
		Query query = entityManager.createQuery(sql);
		query.setParameter("username", username);
		@SuppressWarnings("unchecked")
		List<ITask> unpaidTasks = query.getResultList();
		return unpaidTasks;
	}
	
	private Long getSumOfPaidTasks(String username) {
		String sql = "select count(t) from User u "
				+ "join u.tasks t "
				+ "where username = :username and t.isPaid = true";
		Query query = entityManager.createQuery(sql);
		query.setParameter("username", username);
		return (Long) query.getSingleResult();
	}
	
	private BigDecimal getMembershipDiscount(IUser user, IWorkPlatform platform) {
		String sql = "select m from User u "
				+ "join u.memberships m "
				+ "join m.workPlatform w "
				+ "where u.username = :username and w.id = :platformid";
		Query query = entityManager.createQuery(sql);
		query.setParameter("username", user.getUsername());
		query.setParameter("platformid", platform.getId());
		try {
			IMembership membership = (IMembership) query.getSingleResult();
			return new BigDecimal(membership.getDiscount());
		} catch (NoResultException e) {
			return new BigDecimal(0);
		}
	}

	@Asynchronous
	@Override
	public Future<BillDTO> getBillForUser(String username) throws Exception {
		IUser user = null; 
		try {
			user = getUser(username);
		} catch (NoResultException e) {
			throw new AssignmentException("User does not exist.", e);
		}
		
		BillDTO billDTO = new BillDTO();
		billDTO.setUsername(user.getUsername());
		List<BillDTO.BillPerTask> billPerTasks = new LinkedList<BillDTO.BillPerTask>();
		
		BigDecimal totalPrice = new BigDecimal(0);
		
		for(ITask task : getUnpaidTasks(user.getUsername())) {
			
			BigDecimal processingTimeMin = new BigDecimal(task.getProcessingTime()).divide(new BigDecimal(60000), 0, RoundingMode.HALF_UP);
			BigDecimal cs = priceManagementBean.getPrice(getSumOfPaidTasks(user.getUsername()).intValue()).setScale(2, RoundingMode.HALF_UP);
			IWorkPlatform workPlatform = null;
			for(ITaskWorker taskWorker : task.getTaskProcessing().getTaskWorkers()) {
				workPlatform = taskWorker.getTaskForce().getWorkPlatform();
			}
			BigDecimal cp = workPlatform.getCostsPerWorkUnit().multiply(processingTimeMin).setScale(2, RoundingMode.HALF_UP);
			BigDecimal discount = getMembershipDiscount(user, workPlatform);
			BigDecimal result = cs.add(cp).multiply(new BigDecimal(1).subtract(discount)).setScale(2, RoundingMode.HALF_UP);
			
			BillPerTask billPerTask = billDTO.new BillPerTask();
			billPerTask.setTaskId(task.getId());
			billPerTask.setNumberOfWorkers(task.getAssignedWorkUnits());
			billPerTask.setProcessingCosts(cp);
			billPerTask.setSetupCosts(cs);
			billPerTask.setTaskCosts(result);
			
			billPerTasks.add(billPerTask);
			totalPrice = totalPrice.add(result);
			task.setPaid(true);
			entityManager.persist(task);
		}
		entityManager.flush();
		billDTO.setBills(billPerTasks);
		billDTO.setTotalPrice(totalPrice);
		
		return new AsyncResult<BillDTO>(billDTO);
	}

	@Override
	public List<AuditLogDTO> getAuditLogs() {
		@SuppressWarnings("unchecked")
		List<IAuditLog> auditLogs = entityManager.createQuery("select l from AuditLog l").getResultList();
		List<AuditLogDTO> auditLogDTOs = new LinkedList<AuditLogDTO>();
		for(IAuditLog auditLog : auditLogs) {
			auditLogDTOs.add(new AuditLogDTO(auditLog));
		}
		return auditLogDTOs;
	}

	@Override
	public void clearPriceCache() {
		priceManagementBean.clearCache();
	}
}
