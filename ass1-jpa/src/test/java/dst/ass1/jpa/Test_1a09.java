package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.ModelFactory;
import dst.ass1.jpa.model.TaskStatus;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.JdbcHelper;

public class Test_1a09 extends AbstractTest {

	private Long ent5_1Id;
	private Long ent5_2Id;
	private Long ent5_3Id;
	private Long ent4_1Id;
	private Long ent4_2Id;
	private Long ent4_3Id;

	@Test
	public void testEntity5Entity4Association() {
		Session session = (Session) em.getDelegate();
		DAOFactory daoFactory = new DAOFactory(session);

		List<ITask> ent5_list = daoFactory.getTaskDAO().findAll();

		assertNotNull(ent5_list);
		assertEquals(3, ent5_list.size());

		ITask ent5_1 = daoFactory.getTaskDAO().findById(ent5_1Id);
		ITask ent5_2 = daoFactory.getTaskDAO().findById(ent5_2Id);
		ITask ent5_3 = daoFactory.getTaskDAO().findById(ent5_3Id);

		assertEquals(ent5_1Id, ent5_1.getId());
		assertEquals(ent4_1Id, ent5_1.getTaskProcessing().getId());

		assertEquals(ent5_2Id, ent5_2.getId());
		assertEquals(ent4_2Id, ent5_2.getTaskProcessing().getId());

		assertEquals(ent5_3Id, ent5_3.getId());
		assertEquals(ent4_3Id, ent5_3.getTaskProcessing().getId());

		List<ITaskProcessing> list = daoFactory.getTaskProcessingDAO()
				.findAll();

		assertNotNull(list);
		assertEquals(3, list.size());

		ITaskProcessing ent4_1 = daoFactory.getTaskProcessingDAO()
				.findById(ent4_1Id);
		ITaskProcessing ent4_2 = daoFactory.getTaskProcessingDAO()
				.findById(ent4_2Id);
		ITaskProcessing ent4_3 = daoFactory.getTaskProcessingDAO()
				.findById(ent4_3Id);

		assertEquals(ent4_1Id, ent4_1.getId());
		assertEquals(TaskStatus.SCHEDULED, ent4_1.getStatus());
		assertEquals(ent5_1Id, ent4_1.getTask().getId());

		assertEquals(ent4_2Id, ent4_2.getId());
		assertEquals(TaskStatus.SCHEDULED, ent4_2.getStatus());
		assertEquals(ent5_2Id, ent4_2.getTask().getId());

		assertEquals(ent4_3Id, ent4_3.getId());
		assertEquals(TaskStatus.SCHEDULED, ent4_3.getStatus());
		assertEquals(ent5_3Id, ent4_3.getTask().getId());
	}

	@Test
	public void testEntity5OptionalConstraint() {
		Session session = (Session) em.getDelegate();
		DAOFactory daoFactory = new DAOFactory(session);

		ITask ent5 = daoFactory.getTaskDAO().findById(ent5_1Id);
		assertNotNull(ent5);

		boolean isConstraint = false;
		try {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			ent5.setTaskProcessing(null);
			em.persist(ent5);
			tx.commit();
		} catch (Exception e) {
			if (e.getCause().getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				isConstraint = true;
			}
		}

		assertTrue(isConstraint);
	}

	@Test
	public void testEntity5Entity4AssociationJdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(JdbcHelper.isColumnInTable(Constants.T_TASK,
				Constants.I_PROCESSING, jdbcConnection));
	}

	public void setUpDatabase() {
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();

			ModelFactory modelFactory = new ModelFactory();

			IUser user1 = modelFactory.createUser();
			user1.setUsername(dst.ass1.jpa.util.test.TestData.N_USER_1);

			IUser user2 = modelFactory.createUser();
			user2.setUsername(dst.ass1.jpa.util.test.TestData.N_USER_2);

			em.persist(user1);
			em.persist(user2);

			IMetadata ent6_1 = modelFactory.createMetadata();
			ent6_1.setContext("ctx1");
			ent6_1.addSetting("param1");
			ent6_1.addSetting("param2");
			ent6_1.addSetting("param3");

			IMetadata ent6_2 = modelFactory.createMetadata();
			ent6_2.setContext("ctx2");
			ent6_2.addSetting("param4");

			IMetadata ent6_3 = modelFactory.createMetadata();
			ent6_2.setContext("ctx3");
			ent6_3.addSetting("param5");

			ITaskProcessing ent4_1 = modelFactory.createTaskProcessing();
			ent4_1.setStart(new Date(System.currentTimeMillis() - 36000000));
			ent4_1.setEnd(new Date());
			ent4_1.setStatus(TaskStatus.SCHEDULED);

			ITaskProcessing ent4_2 = modelFactory.createTaskProcessing();
			ent4_2.setStart(new Date());
			ent4_2.setStatus(TaskStatus.SCHEDULED);

			ITaskProcessing ent4_3 = modelFactory.createTaskProcessing();
			ent4_3.setStart(new Date());
			ent4_3.setStatus(TaskStatus.SCHEDULED);

			ITask ent5_1 = modelFactory.createTask();
			ent5_1.setMetadata(ent6_1);
			ent5_1.setTaskProcessing(ent4_1);
			ent5_1.setUser(user1);
			user1.addTask(ent5_1);

			ITask ent5_2 = modelFactory.createTask();
			ent5_2.setMetadata(ent6_2);
			ent5_2.setTaskProcessing(ent4_2);
			ent5_2.setUser(user2);
			user2.addTask(ent5_2);

			ITask ent5_3 = modelFactory.createTask();
			ent5_3.setMetadata(ent6_3);
			ent5_3.setTaskProcessing(ent4_3);
			ent5_3.setUser(user1);
			user1.addTask(ent5_3);

			ent4_1.setTask(ent5_1);
			ent4_2.setTask(ent5_2);
			ent4_3.setTask(ent5_3);

			em.persist(ent6_1);
			em.persist(ent6_2);
			em.persist(ent6_3);
			em.persist(ent4_1);
			em.persist(ent4_2);
			em.persist(ent4_3);
			em.persist(ent5_1);
			em.persist(ent5_2);
			em.persist(ent5_3);

			tx.commit();

			ent5_1Id = ent5_1.getId();
			ent5_2Id = ent5_2.getId();
			ent5_3Id = ent5_3.getId();
			ent4_1Id = ent4_1.getId();
			ent4_2Id = ent4_2.getId();
			ent4_3Id = ent4_3.getId();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
