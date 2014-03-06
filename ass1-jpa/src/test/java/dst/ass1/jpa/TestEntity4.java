package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.model.TaskStatus;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class TestEntity4 extends AbstractTest {

	private Long ent4_1Id;
	private Long ent4_2Id;
	private Long ent4_3Id;
	private Long ent3_1Id;
	private Long ent3_2Id;
	private Long ent3_3Id;

	@Test
	public void testEntity() {
		Session session = (Session) em.getDelegate();
		DAOFactory daoFactory = new DAOFactory(session);

		List<ITaskProcessing> list = daoFactory.getTaskProcessingDAO()
				.findAll();

		assertNotNull(list);
		assertEquals(3, list.size());

		ITaskProcessing ent4_1 = daoFactory.getTaskProcessingDAO().findById(
				ent4_1Id);
		ITaskProcessing ent4_2 = daoFactory.getTaskProcessingDAO().findById(
				ent4_2Id);
		ITaskProcessing ent4_3 = daoFactory.getTaskProcessingDAO().findById(
				ent4_3Id);

		assertEquals(ent4_1Id, ent4_1.getId());
		assertNotNull(ent4_1.getStart());
		assertNotNull(ent4_1.getEnd());
		assertEquals(TaskStatus.SCHEDULED, ent4_1.getStatus());
		assertNotNull(ent4_1.getTaskWorkers());
		assertEquals(1, ent4_1.getTaskWorkers().size());
		assertEquals(ent3_1Id, ent4_1.getTaskWorkers().get(0).getId());

		assertEquals(ent4_2Id, ent4_2.getId());
		assertNotNull(ent4_2.getStart());
		assertNull(ent4_2.getEnd());
		assertEquals(TaskStatus.SCHEDULED, ent4_2.getStatus());
		assertNotNull(ent4_2.getTaskWorkers());
		assertEquals(1, ent4_2.getTaskWorkers().size());
		assertEquals(ent3_2Id, ent4_2.getTaskWorkers().get(0).getId());

		assertEquals(ent4_3Id, ent4_3.getId());
		assertNotNull(ent4_3.getStart());
		assertNull(ent4_3.getEnd());
		assertEquals(TaskStatus.SCHEDULED, ent4_3.getStatus());
		assertNotNull(ent4_3.getTaskWorkers());
		assertEquals(1, ent4_3.getTaskWorkers().size());
		assertEquals(ent3_3Id, ent4_3.getTaskWorkers().get(0).getId());

	}

	@Test
	public void testEntityJdbc() throws SQLException, ClassNotFoundException {
		String sql = "SELECT id, end, start, status FROM "
				+ Constants.T_TASKPROCESSING + " ORDER BY id ASC";
		Statement stmt = jdbcConnection.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {

			long id = rs.getLong("id");

			if (id == ent4_1Id.longValue()) {
				assertEquals((long) ent4_1Id, rs.getLong("id"));
				assertNotNull(rs.getDate("end"));
				assertNotNull(rs.getDate("start"));
				assertEquals("SCHEDULED", rs.getString("status"));
			} else if (id == ent4_2Id.longValue()) {
				assertEquals((long) ent4_2Id, rs.getLong("id"));
				assertNull(rs.getDate("end"));
				assertNotNull(rs.getDate("start"));
				assertEquals("SCHEDULED", rs.getString("status"));
			} else if (id == ent4_3Id.longValue()) {
				assertEquals((long) ent4_3Id, rs.getLong("id"));
				assertNull(rs.getDate("end"));
				assertNotNull(rs.getDate("start"));
				assertEquals("SCHEDULED", rs.getString("status"));
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

			IExpert ent7_1 = modelFactory.createExpert();

			ITaskWorker ent3_1 = modelFactory.createTaskWorker();
			ent3_1.setName(TestData.N_ENT3_1);
			ent3_1.setWorkUnitCapacity(2);
			ent3_1.setLocation("AUT-VIE-location1");
			ent3_1.setJoinedDate(new Date(0));
			ent3_1.setLastTraining(new Date(0));

			ITaskWorker ent3_2 = modelFactory.createTaskWorker();
			ent3_2.setName(TestData.N_ENT3_2);
			ent3_2.setWorkUnitCapacity(3);
			ent3_2.setLocation("AUT-VIE-location2");
			ent3_2.setJoinedDate(new Date(0));
			ent3_2.setLastTraining(new Date(0));

			ITaskWorker ent3_3 = modelFactory.createTaskWorker();
			ent3_3.setName(TestData.N_ENT3_3);
			ent3_3.setWorkUnitCapacity(4);
			ent3_3.setLocation("AUT-VIE-location3");
			ent3_3.setJoinedDate(new Date(0));
			ent3_3.setLastTraining(new Date(0));

			ITaskForce ent2_1 = modelFactory.createTaskForce();
			ent2_1.setExpert(ent7_1);
			ent2_1.setName(TestData.N_ENT2_1);
			ent2_1.setLastMeeting(new Date());
			ent2_1.setNextMeeting(new Date());
			ent7_1.addAdvisedTaskForce(ent2_1);

			ent2_1.addTaskWorker(ent3_1);
			ent2_1.addTaskWorker(ent3_2);
			ent2_1.addTaskWorker(ent3_3);
			ent3_1.setTaskForce(ent2_1);
			ent3_2.setTaskForce(ent2_1);
			ent3_3.setTaskForce(ent2_1);

			IWorkPlatform ent1_1 = modelFactory.createPlatform();
			ent2_1.setWorkPlatform(ent1_1);

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

			ent4_1.addWorker(ent3_1);
			ent4_2.addWorker(ent3_2);
			ent4_3.addWorker(ent3_3);

			ent3_1.addTaskProcessing(ent4_1);
			ent3_2.addTaskProcessing(ent4_2);
			ent3_3.addTaskProcessing(ent4_3);

			em.persist(ent1_1);
			em.persist(ent7_1);
			em.persist(ent2_1);
			em.persist(ent4_1);
			em.persist(ent4_2);
			em.persist(ent4_3);
			em.persist(ent3_1);
			em.persist(ent3_2);
			em.persist(ent3_3);

			tx.commit();

			ent4_1Id = ent4_1.getId();
			ent4_2Id = ent4_2.getId();
			ent4_3Id = ent4_3.getId();
			ent3_1Id = ent3_1.getId();
			ent3_2Id = ent3_2.getId();
			ent3_3Id = ent3_3.getId();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
