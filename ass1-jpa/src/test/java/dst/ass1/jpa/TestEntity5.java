package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
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
import dst.ass1.jpa.model.TaskStatus;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class TestEntity5 extends AbstractTest {

	private Long ent5_1Id;
	private Long ent5_2Id;
	private Long ent5_3Id;
	private Long ent6_1Id;
	private Long ent6_2Id;
	private Long ent6_3Id;
	private Long ent4_1Id;
	private Long ent4_2Id;
	private Long ent4_3Id;
	private Long user1Id;
	private Long user2Id;

	@Test
	public void testEntity() {
		Session session = (Session) em.getDelegate();
		DAOFactory daoFactory = new DAOFactory(session);

		List<ITask> list = daoFactory.getTaskDAO().findAll();

		assertNotNull(list);
		assertEquals(3, list.size());

		ITask ent5_1 = daoFactory.getTaskDAO().findById(ent5_1Id);
		ITask ent5_2 = daoFactory.getTaskDAO().findById(ent5_2Id);
		ITask ent5_3 = daoFactory.getTaskDAO().findById(ent5_3Id);

		assertEquals(ent5_1Id, ent5_1.getId());
		assertEquals(false, ent5_1.isPaid());
		assertEquals(ent6_1Id, ent5_1.getMetadata().getId());
		assertEquals(ent4_1Id, ent5_1.getTaskProcessing().getId());
		assertEquals(user1Id, ((IPerson) ent5_1.getUser()).getId());
		assertEquals(new Integer(0), ent5_1.getProcessingTime());

		assertEquals(ent5_2Id, ent5_2.getId());
		assertEquals(false, ent5_2.isPaid());
		assertEquals(ent6_2Id, ent5_2.getMetadata().getId());
		assertEquals(ent4_2Id, ent5_2.getTaskProcessing().getId());
		assertEquals(user2Id, ((IPerson) ent5_2.getUser()).getId());
		assertEquals(new Integer(0), ent5_2.getProcessingTime());

		assertEquals(ent5_3Id, ent5_3.getId());
		assertEquals(false, ent5_3.isPaid());
		assertEquals(ent6_3Id, ent5_3.getMetadata().getId());
		assertEquals(ent4_3Id, ent5_3.getTaskProcessing().getId());
		assertEquals(user1Id, ((IPerson) ent5_3.getUser()).getId());
		assertEquals(new Integer(0), ent5_3.getProcessingTime());

	}

	@Test
	public void testEntityJdbc() throws ClassNotFoundException, SQLException {
		String sql = "SELECT id, " +
				Constants.M_ISPAID + ", " +
				Constants.I_METADATA + ", " +
				Constants.I_PROCESSING + ", " +
				Constants.I_USER + " " +
				"FROM " + Constants.T_TASK + " ORDER BY id ASC";
		Statement stmt = jdbcConnection.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {

			long id = rs.getLong("id");

			if (id == ent5_1Id.longValue()) {
				assertEquals((long) ent5_1Id, rs.getLong("id"));
				assertFalse(rs.getBoolean(Constants.M_ISPAID));
				assertEquals((long) ent6_1Id,
						rs.getLong(Constants.I_METADATA));
				assertEquals((long) ent4_1Id, rs.getLong(Constants.I_PROCESSING));
				assertEquals((long) user1Id, rs.getLong(Constants.I_USER));
			} else if (id == ent5_2Id.longValue()) {
				assertEquals((long) ent5_2Id, rs.getLong("id"));
				assertFalse(rs.getBoolean(Constants.M_ISPAID));
				assertEquals((long) ent6_2Id,
						rs.getLong(Constants.I_METADATA));
				assertEquals((long) ent4_2Id, rs.getLong(Constants.I_PROCESSING));
				assertEquals((long) user2Id, rs.getLong(Constants.I_USER));
			} else if (id == ent5_3Id.longValue()) {
				assertEquals((long) ent5_3Id, rs.getLong("id"));
				assertFalse(rs.getBoolean(Constants.M_ISPAID));
				assertEquals((long) ent6_3Id,
						rs.getLong(Constants.I_METADATA));
				assertEquals((long) ent4_3Id, rs.getLong(Constants.I_PROCESSING));
				assertEquals((long) user1Id, rs.getLong(Constants.I_USER));
			} else {
				fail("Unexpected entity found!");
			}

		}

		rs.close();
		stmt.close();
	}

	protected void setUpDatabase() {

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			IMetadata ent6_1 = modelFactory.createMetadata();
			ent6_1.setContext(TestData.N_ENT6_1);
			ent6_1.addSetting("param1");
			ent6_1.addSetting("param2");
			ent6_1.addSetting("param3");

			IMetadata ent6_2 = modelFactory.createMetadata();
			ent6_2.setContext(TestData.N_ENT6_2);
			ent6_2.addSetting("param4");

			IMetadata ent6_3 = modelFactory.createMetadata();
			ent6_3.setContext(TestData.N_ENT6_3);
			ent6_3.addSetting("param5");

			em.persist(ent6_1);
			em.persist(ent6_2);
			em.persist(ent6_3);

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

			IUser user1 = modelFactory.createUser();
			IUser user2 = modelFactory.createUser();

			user1.setUsername("u1");
			user2.setUsername("u2");

			// Entity #5
			ITask ent5_1 = modelFactory.createTask();
			ent5_1.setAssignedWorkUnits(2);
			ent5_1.setProcessingTime(0);
			ent5_1.setMetadata(ent6_1);
			ent5_1.setTaskProcessing(ent4_1);
			ent5_1.setUser(user1);
			user1.addTask(ent5_1);

			ITask ent5_2 = modelFactory.createTask();
			ent5_2.setAssignedWorkUnits(3);
			ent5_2.setProcessingTime(0);
			ent5_2.setMetadata(ent6_2);
			ent5_2.setTaskProcessing(ent4_2);
			ent5_2.setUser(user2);
			user2.addTask(ent5_2);

			ITask ent5_3 = modelFactory.createTask();
			ent5_3.setAssignedWorkUnits(4);
			ent5_3.setProcessingTime(0);
			ent5_3.setMetadata(ent6_3);
			ent5_3.setTaskProcessing(ent4_3);
			ent5_3.setUser(user1);
			user1.addTask(ent5_3);

			ent4_1.setTask(ent5_1);
			ent4_2.setTask(ent5_2);
			ent4_3.setTask(ent5_3);

			em.persist(ent4_1);
			em.persist(ent4_2);
			em.persist(ent4_3);

			em.persist(user1);
			em.persist(user2);
			em.persist(ent5_1);
			em.persist(ent5_2);
			em.persist(ent5_3);

			tx.commit();

			ent5_1Id = ent5_1.getId();
			ent5_2Id = ent5_2.getId();
			ent5_3Id = ent5_3.getId();
			ent6_1Id = ent6_1.getId();
			ent6_2Id = ent6_2.getId();
			ent6_3Id = ent6_3.getId();
			ent4_1Id = ent4_1.getId();
			ent4_2Id = ent4_2.getId();
			ent4_3Id = ent4_3.getId();
			user1Id = ((IPerson) user1).getId();
			user2Id = ((IPerson) user2).getId();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
