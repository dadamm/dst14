package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
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
import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.model.TaskStatus;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class TestEntity3 extends AbstractTest {

	private Long ent2_1Id;
	private Long ent2_2Id;
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

		List<ITaskWorker> entities = daoFactory.getTaskWorkerDAO().findAll();

		assertNotNull(entities);

		ITaskWorker ent2_1_ent3_1 = daoFactory.getTaskWorkerDAO().findById(
				ent3_1Id);
		ITaskWorker ent2_1_ent3_2 = daoFactory.getTaskWorkerDAO().findById(
				ent3_2Id);
		ITaskWorker ent2_2_ent3_1 = daoFactory.getTaskWorkerDAO().findById(
				ent3_3Id);

		assertEquals(TestData.N_ENT3_1, ent2_1_ent3_1.getName());
		assertEquals(new Integer(2), ent2_1_ent3_1.getWorkUnitCapacity());
		assertNotNull(ent2_1_ent3_1.getJoinedDate());
		assertNotNull(ent2_1_ent3_1.getLastTraining());

		assertEquals("AUT-VIE-location1", ent2_1_ent3_1.getLocation());
		assertNotNull(ent2_1_ent3_1.getTaskForce());
		assertEquals(ent2_1Id, ent2_1_ent3_1.getTaskForce().getId());
		assertEquals(1, ent2_1_ent3_1.getTaskProcessings().size());
		assertEquals(ent4_1Id, ent2_1_ent3_1.getTaskProcessings().get(0).getId());

		assertEquals(TestData.N_ENT3_2, ent2_1_ent3_2.getName());
		assertEquals(new Integer(3), ent2_1_ent3_2.getWorkUnitCapacity());
		assertNotNull(ent2_1_ent3_2.getJoinedDate());
		assertNotNull(ent2_1_ent3_2.getLastTraining());

		assertEquals("AUT-VIE-location2", ent2_1_ent3_2.getLocation());
		assertNotNull(ent2_1_ent3_2.getTaskForce());
		assertEquals(ent2_1Id, ent2_1_ent3_2.getTaskForce().getId());
		assertEquals(1, ent2_1_ent3_2.getTaskProcessings().size());
		assertEquals(ent4_2Id, ent2_1_ent3_2.getTaskProcessings().get(0).getId());

		assertEquals(TestData.N_ENT3_3, ent2_2_ent3_1.getName());
		assertEquals(new Integer(4), ent2_2_ent3_1.getWorkUnitCapacity());
		assertNotNull(ent2_2_ent3_1.getJoinedDate());
		assertNotNull(ent2_2_ent3_1.getLastTraining());

		assertEquals("AUT-VIE-location3", ent2_2_ent3_1.getLocation());
		assertNotNull(ent2_2_ent3_1.getTaskForce());
		assertEquals(ent2_2Id, ent2_2_ent3_1.getTaskForce().getId());
		assertEquals(1, ent2_2_ent3_1.getTaskProcessings().size());
		assertEquals(ent4_3Id, ent2_2_ent3_1.getTaskProcessings().get(0).getId());

	}

	@Test
	public void testEntityJdbc() throws ClassNotFoundException, SQLException {
		String sql = "SELECT id, " + Constants.M_WORKUNITCAPACITY + ", "
				+ Constants.M_JOINEDDATE + ", " + Constants.M_LASTTRAINING
				+ ", " + Constants.I_TASKFORCE + ", " + "location, " + "name "
				+ "FROM " + Constants.T_TASKWORKER + " ORDER BY id ASC";
		Statement stmt = jdbcConnection.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {

			long id = rs.getLong("id");

			if (id == ent3_1Id.longValue()) {
				assertEquals((long) ent3_1Id, rs.getLong("id"));
				assertEquals(2, rs.getLong(Constants.M_WORKUNITCAPACITY));
				assertNotNull(rs.getDate(Constants.M_JOINEDDATE));
				assertNotNull(rs.getDate(Constants.M_LASTTRAINING));
				assertEquals("AUT-VIE-location1", rs.getString("location"));
				assertEquals(TestData.N_ENT3_1, rs.getString("name"));
				assertEquals((long) ent2_1Id, rs.getLong(Constants.I_TASKFORCE));
			} else if (id == ent3_2Id.longValue()) {
				assertEquals((long) ent3_2Id, rs.getLong("id"));
				assertEquals(3, rs.getLong(Constants.M_WORKUNITCAPACITY));
				assertNotNull(rs.getDate(Constants.M_JOINEDDATE));
				assertNotNull(rs.getDate(Constants.M_LASTTRAINING));
				assertEquals("AUT-VIE-location2", rs.getString("location"));
				assertEquals(TestData.N_ENT3_2, rs.getString("name"));
				assertEquals((long) ent2_1Id, rs.getLong(Constants.I_TASKFORCE));
			} else if (id == ent3_3Id.longValue()) {
				assertEquals((long) ent3_3Id, rs.getLong("id"));
				assertEquals(4, rs.getLong(Constants.M_WORKUNITCAPACITY));
				assertNotNull(rs.getDate(Constants.M_JOINEDDATE));
				assertNotNull(rs.getDate(Constants.M_LASTTRAINING));
				assertEquals("AUT-VIE-location3", rs.getString("location"));
				assertEquals(TestData.N_ENT3_3, rs.getString("name"));
				assertEquals((long) ent2_2Id, rs.getLong(Constants.I_TASKFORCE));
			} else {
				fail("Unexpected entity found!");
			}

		}

		rs.close();
		stmt.close();
	}

	@Test
	public void testAssociationJdbc() throws ClassNotFoundException,
			SQLException {
		String sql = "SELECT " + Constants.I_PROCESSINGS + ", "
				+ Constants.I_TASKWORKERS + " " + "FROM "
				+ Constants.J_PROCESSING_TASKWORKER;
		Statement stmt = jdbcConnection.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {

			long id = rs.getLong(Constants.I_PROCESSINGS);

			if (id == ent4_1Id.longValue()) {
				assertEquals((long) ent4_1Id,
						rs.getLong(Constants.I_PROCESSINGS));
				assertEquals((long) ent3_1Id,
						rs.getLong(Constants.I_TASKWORKERS));
			} else if (id == ent4_2Id.longValue()) {
				assertEquals((long) ent4_2Id,
						rs.getLong(Constants.I_PROCESSINGS));
				assertEquals((long) ent3_2Id,
						rs.getLong(Constants.I_TASKWORKERS));
			} else if (id == ent4_3Id.longValue()) {
				assertEquals((long) ent4_3Id,
						rs.getLong(Constants.I_PROCESSINGS));
				assertEquals((long) ent3_3Id,
						rs.getLong(Constants.I_TASKWORKERS));
			} else {
				fail("Unexpected Entity found!");
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
			IExpert ent7_2 = modelFactory.createExpert();

			// Entity #3
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

			// Entity #2
			ITaskForce ent2_1 = modelFactory.createTaskForce();
			ent2_1.setExpert(ent7_1);
			ent2_1.setName(TestData.N_ENT2_1);
			ent2_1.setLastMeeting(new Date());
			ent2_1.setNextMeeting(new Date());

			ITaskForce ent2_2 = modelFactory.createTaskForce();
			ent2_2.setExpert(ent7_2);
			ent2_2.setName(TestData.N_ENT2_2);
			ent2_2.setLastMeeting(new Date());
			ent2_2.setNextMeeting(new Date());

			ent2_1.addComposedOf(ent2_2);
			ent2_2.addPartOf(ent2_1);

			ent7_1.addAdvisedTaskForce(ent2_1);
			ent7_2.addAdvisedTaskForce(ent2_2);

			ent2_1.addTaskWorker(ent3_1);
			ent2_1.addTaskWorker(ent3_2);
			ent3_1.setTaskForce(ent2_1);
			ent3_2.setTaskForce(ent2_1);

			ent2_2.addTaskWorker(ent3_3);
			ent3_3.setTaskForce(ent2_2);

			IWorkPlatform ent1_1 = modelFactory.createPlatform();
			ent2_1.setWorkPlatform(ent1_1);
			ent2_2.setWorkPlatform(ent1_1);

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
			em.persist(ent7_2);
			em.persist(ent2_1);
			em.persist(ent2_2);
			em.persist(ent4_1);
			em.persist(ent4_2);
			em.persist(ent4_3);
			em.persist(ent3_1);
			em.persist(ent3_2);
			em.persist(ent3_3);

			tx.commit();

			ent3_1Id = ent3_1.getId();
			ent3_2Id = ent3_2.getId();
			ent3_3Id = ent3_3.getId();
			ent2_1Id = ent2_1.getId();
			ent2_2Id = ent2_2.getId();
			ent4_1Id = ent4_1.getId();
			ent4_2Id = ent4_2.getId();
			ent4_3Id = ent4_3.getId();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
