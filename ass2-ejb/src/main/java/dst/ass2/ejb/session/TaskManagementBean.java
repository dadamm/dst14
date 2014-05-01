package dst.ass2.ejb.session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.TaskStatus;
import dst.ass1.jpa.model.impl.Metadata;
import dst.ass1.jpa.model.impl.Task;
import dst.ass1.jpa.model.impl.TaskProcessing;
import dst.ass1.jpa.model.impl.User;
import dst.ass2.ejb.dto.AssignmentDTO;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.interfaces.ITaskManagementBean;

@Stateful
@Remote(ITaskManagementBean.class)
public class TaskManagementBean implements ITaskManagementBean {
	
	@PersistenceContext private EntityManager entityManager;
	
	private IUser currentUser;
	private List<AssignmentDTO> assigments;

	public TaskManagementBean() {
		this.assigments = new LinkedList<AssignmentDTO>();
	}
	
	private List<ITaskWorker> getAllTaskWorkers() {
		String sql = "select w from TaskWorker w";
		
		@SuppressWarnings("unchecked")
		List<ITaskWorker> allTaskWorkers = entityManager.createQuery(sql).getResultList();
		return allTaskWorkers;
	}
	
	private boolean isTaskWorkerAvailable(ITaskWorker taskWorker) {
		if(taskWorker.getTaskProcessings().isEmpty()) {
			return true;
		} else {
			for(ITaskProcessing taskProcessing : taskWorker.getTaskProcessings()) {
				if(taskProcessing.getStatus().equals(TaskStatus.PROCESSING) || taskProcessing.getStatus().equals(TaskStatus.SCHEDULED)) {
					return false;
				}
			}
			return true;
		}
	}
	
	@Override
	public void addTask(Long platformId, Integer numWorkUnits, String context, List<String> settings) throws AssignmentException {
		List<ITaskWorker> allTaskWorkers = getAllTaskWorkers();
		List<ITaskWorker> taskWorkers = new LinkedList<ITaskWorker>();
		
		Integer sumOfWorkUnits = 0;
		
		for(ITaskWorker taskWorker : allTaskWorkers) {
			if(taskWorker.getTaskForce().getWorkPlatform().getId().equals(platformId) && isTaskWorkerAvailable(taskWorker)) {
				sumOfWorkUnits += taskWorker.getWorkUnitCapacity();
				taskWorkers.add(taskWorker);
			}
		}
		
		if(numWorkUnits <= sumOfWorkUnits) {
			sumOfWorkUnits = 0;
			List<Long> taskWorkerIds = new LinkedList<Long>();
			for(ITaskWorker taskWorker : taskWorkers) {
				if(sumOfWorkUnits < numWorkUnits) {
					taskWorkerIds.add(taskWorker.getId());
				}
			}
			this.assigments.add(new AssignmentDTO(platformId, numWorkUnits, context, settings, taskWorkerIds));
		} else {
			throw new AssignmentException("Platform has not enough work units for task");
		}
		
	}

	@Override
	public void login(String username, String password) throws AssignmentException {
		currentUser = null;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] passwordHash = md.digest(password.getBytes());
			
			String sql = "select u from User u where u.username = :username";
			IUser user = (IUser) entityManager.createQuery(sql).setParameter("username", username).getSingleResult();
			if(Arrays.equals(user.getPassword(), passwordHash)) {
				currentUser = user;
			} else {
				throw new AssignmentException("Cannot log in. Wrong username or password.");
			}
		} catch (NoSuchAlgorithmException e) {
			throw new AssignmentException("Not possible to generate md5.", e);
		}
	}

	@Override
	public void removeTasksForPlatform(Long platformId) {
		List<AssignmentDTO> assToRemove = new LinkedList<AssignmentDTO>();
		for(AssignmentDTO ass : assigments) {
			if(ass.getPlatformId().equals(platformId.longValue())) {
				assToRemove.add(ass);
			}
		}
		assigments.removeAll(assToRemove);
	}

	@Override
	@Remove(retainIfException = true)
	public void submitAssignments() throws AssignmentException {
		if(currentUser == null) {
			throw new AssignmentException("No user is logged in.");
		}
		
		HashMap<Long, ITaskWorker> taskWorkerMap = new HashMap<Long, ITaskWorker>();
		for(ITaskWorker taskWorker : getAllTaskWorkers()) {
			taskWorkerMap.put(taskWorker.getId(), taskWorker);
		}
		
		for(AssignmentDTO ass : assigments) {
			for(Long workerId : ass.getWorkerIds()) {
				ITaskWorker worker = taskWorkerMap.get(workerId);
				if(worker != null) {
					if(isTaskWorkerAvailable(worker)) {
						entityManager.lock(worker, LockModeType.PESSIMISTIC_READ);
					} else {
						throw new AssignmentException("TaskWorker with id " + workerId + " has other taskprocessings");
					}
				} else {
					throw new AssignmentException("TaskWorker with id " + workerId + " does not exist.");
				}
			}
		}
		currentUser = entityManager.find(User.class, ((User)currentUser).getId());
		Date currentDate = new Date();
		for(AssignmentDTO ass : assigments) {
			IMetadata metadata = new Metadata();
			metadata.setContext(ass.getContext());
			metadata.setSettings(ass.getSettings());
			
			ITask task = new Task();
			task.setMetadata(metadata);
			task.setUser(currentUser);
			task.setPaid(false);
			
			ITaskProcessing taskProcessing = new TaskProcessing();
			taskProcessing.setTask(task);
			taskProcessing.setStart(currentDate);
			taskProcessing.setStatus(TaskStatus.SCHEDULED);
			
			task.setTaskProcessing(taskProcessing);
			
			for(Long workerId : ass.getWorkerIds()) {
				ITaskWorker taskWorker = taskWorkerMap.get(workerId);
				taskWorker.addTaskProcessing(taskProcessing);
				taskProcessing.addWorker(taskWorker);
				entityManager.persist(taskProcessing);
				entityManager.persist(taskWorker);
			}
			currentUser.addTask(task);
			entityManager.persist(metadata);
			entityManager.persist(task);
		}
		entityManager.persist(currentUser);
		entityManager.flush();
//		entityManager.getTransaction().commit();
		assigments.clear();
	}

	@Override
	public List<AssignmentDTO> getCache() {
		return this.assigments;
	}

}
