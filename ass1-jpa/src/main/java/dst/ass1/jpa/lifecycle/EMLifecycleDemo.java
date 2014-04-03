package dst.ass1.jpa.lifecycle;

import java.security.NoSuchAlgorithmException;

import javax.persistence.EntityManager;

import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.ModelFactory;

public class EMLifecycleDemo {

	private EntityManager em;
	private ModelFactory modelFactory;

	public EMLifecycleDemo(EntityManager em, ModelFactory modelFactory) {
		this.em = em;
		this.modelFactory = modelFactory;
	}

	/**
	 * Method to illustrate the persistence lifecycle. EntityManager is opened and
	 * closed by the Test-Environment!
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public void demonstrateEntityMangerLifecycle()
			throws NoSuchAlgorithmException {
		
		/*
		 * State: NEW
		 * The entities we want to persist are initialized, but not associated
		 * with the EntityManager and has no reference to the database.
		 */
		ITask task = modelFactory.createTask();
		
		IUser user = modelFactory.createUser();
		user.setUsername("username1");
		
		task.setUser(user);
		
		/*
		 * Status: MANAGED
		 * After persisting entity, it becomes managed. The Entity will be stored
		 * into the database.
		 * When properties in the database are changed the EntityManager updates the
		 * values in the database.
		 */
		em.persist(task);
		
		/* 
		 * Status: REMOVED
		 * The persisted entity will be marked as removed. At the next commit
		 * it will be removed physically.
		 */
		em.remove(task);
		
		/*
		 * Status: DETACHED
		 * The entity become this status, when it has been disconnected from the EntityManager.
		 * It has no persistence context.
		 * All entities become detached, when the EntityManager is closed.
		 */
		em.detach(task);
	}

}
