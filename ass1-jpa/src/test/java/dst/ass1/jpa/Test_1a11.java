package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.IPerson;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.JdbcHelper;

public class Test_1a11 extends AbstractTest {

	private Long user1Id;
	private Long user2Id;

	private Long ent5_1Id;
	private Long ent5_2Id;
	private Long ent5_3Id;

	@Test
	public void testUserEntity5Association() {

		System.out.println();

		Session session = (Session) em.getDelegate();
		DAOFactory daoFactory = new DAOFactory(session);

		IUser user = daoFactory.getUserDAO().findById(user1Id);
		assertNotNull(user);
		assertNotNull(user.getTasks());

		List<Long> ent5Ids = getTaskIds(user.getTasks());

		assertEquals(2, ent5Ids.size());

		assertTrue(ent5Ids.contains(ent5_1Id));
		assertTrue(ent5Ids.contains(ent5_3Id));

		ITask ent5_1 = daoFactory.getTaskDAO().findById(ent5_1Id);
		ITask ent5_2 = daoFactory.getTaskDAO().findById(ent5_2Id);

		assertNotNull(ent5_1);
		assertNotNull(ent5_2);

		assertEquals(user1Id, ((IPerson) ent5_1.getUser()).getId());
		assertEquals(user2Id, ((IPerson) ent5_2.getUser()).getId());
	}

	@Test
	public void testUserEntity5AssociationJdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(JdbcHelper.isColumnInTable(Constants.T_TASK,
				Constants.I_USER, jdbcConnection));
	}

	public void setUpDatabase() {

		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();

			IUser user1 = modelFactory.createUser();
			user1.setUsername(dst.ass1.jpa.util.test.TestData.N_USER_1);

			IUser user2 = modelFactory.createUser();
			user2.setUsername(dst.ass1.jpa.util.test.TestData.N_USER_2);

			em.persist(user1);
			em.persist(user2);

			IMetadata ent6_1 = modelFactory.createMetadata();
			IMetadata ent6_2 = modelFactory.createMetadata();
			IMetadata ent6_3 = modelFactory.createMetadata();

			em.persist(ent6_1);
			em.persist(ent6_2);
			em.persist(ent6_3);

			ITaskProcessing ent4_1 = modelFactory.createTaskProcessing();
			ITaskProcessing ent4_2 = modelFactory.createTaskProcessing();
			ITaskProcessing ent4_3 = modelFactory.createTaskProcessing();

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

			em.persist(user1);
			em.persist(user2);
			em.persist(ent5_1);
			em.persist(ent5_2);
			em.persist(ent5_3);

			tx.commit();

			user1Id = ((IPerson) user1).getId();
			user2Id = ((IPerson) user2).getId();

			ent5_1Id = ent5_1.getId();
			ent5_2Id = ent5_2.getId();
			ent5_3Id = ent5_3.getId();

		} catch (Exception e) {
			tx.rollback();
			fail(e.getMessage());
		}

	}
}
