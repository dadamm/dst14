package dst.ass2.ejb.session;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.model.impl.TaskWorker;
import dst.ass2.ejb.dto.AuditLogDTO;
import dst.ass2.ejb.dto.BillDTO;
import dst.ass2.ejb.management.PriceManagementBean;
import dst.ass2.ejb.management.interfaces.IPriceManagementBean;
import dst.ass2.ejb.model.IAuditLog;
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

	@Override
	public Future<BillDTO> getBillForUser(String username) throws Exception {
		@SuppressWarnings("unchecked")
		List<ITask> tasks = entityManager.createQuery("select t from User u join u.tasks t join t.taskProcessing tp where t.isPaid = false and tp.status = 'FINISHED'").getResultList();
		
		BigDecimal schedulingCosts = priceManagementBean.getPrice(tasks.size());
		BigDecimal sumCostsPerWorkUnit = null;
		for(ITask task : tasks) {
			List<ITaskWorker> taskWorkers = task.getTaskProcessing().getTaskWorkers();
			for(ITaskWorker worker : taskWorkers) {
				IWorkPlatform workPlatform = worker.getTaskForce().getWorkPlatform();
				if(sumCostsPerWorkUnit == null) {
					sumCostsPerWorkUnit = workPlatform.getCostsPerWorkUnit();
				} else {
					sumCostsPerWorkUnit.add(workPlatform.getCostsPerWorkUnit());
				}
			}
		}
		return null;
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
