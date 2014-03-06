package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
import dst.ass1.jpa.dao.ITaskForceDAO;
import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.IPerson;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class TestEntity2 extends AbstractTest {

	private Long ent1_1_id;
	private Long ent2_1_id;
	private Long ent2_2_id;
	private Long ent3_1_id;
	private Long ent3_2_id;
	private Long ent3_3_id;
	private Long ent7_1_id;
	private Long ent7_2_id;

	@Test
	public void testEntity() {
		Session session = (Session) em.getDelegate();
		DAOFactory daoFactory = new DAOFactory(session);

		ITaskForceDAO dao = daoFactory.getTaskForceDAO();
		List<ITaskForce> entities = dao.findAll();

		assertNotNull(entities);
		assertEquals(2, entities.size());

		ITaskForce ent2_1 = daoFactory.getTaskForceDAO().findById(ent2_1_id);
		ITaskForce ent2_2 = daoFactory.getTaskForceDAO().findById(ent2_2_id);

		assertEquals(ent2_1_id, ent2_1.getId());
		assertEquals(TestData.N_ENT2_1, ent2_1.getName());
		assertNotNull(ent2_1.getLastMeeting());
		assertNotNull(ent2_1.getNextMeeting());

		assertEquals(ent2_2_id, ent2_2.getId());
		assertEquals(TestData.N_ENT2_2, ent2_2.getName());
		assertNotNull(ent2_2.getLastMeeting());
		assertNotNull(ent2_2.getNextMeeting());

		assertNotNull(ent2_1.getTaskWorkers());
		assertEquals(2, ent2_1.getTaskWorkers().size());

		List<Long> ent2_1_ids = getWorkerIds(ent2_1.getTaskWorkers());
		assertTrue(ent2_1_ids.contains(ent3_1_id));
		assertTrue(ent2_1_ids.contains(ent3_2_id));

		assertNotNull(ent2_2.getTaskWorkers());
		assertEquals(1, ent2_2.getTaskWorkers().size());

		List<Long> ent2_2_ids = getWorkerIds(ent2_2.getTaskWorkers());
		assertTrue(ent2_2_ids.contains(ent3_3_id));

		assertNotNull(ent2_1.getExpert());
		assertEquals(ent7_1_id, ((IPerson) ent2_1.getExpert()).getId());

		assertNotNull(ent2_2.getExpert());
		assertEquals(ent7_2_id, ((IPerson) ent2_2.getExpert()).getId());

		assertNotNull(ent2_1.getWorkPlatform());
		assertEquals(ent1_1_id, ent2_1.getWorkPlatform().getId());

		assertNotNull(ent2_2.getWorkPlatform());
		assertEquals(ent1_1_id, ent2_2.getWorkPlatform().getId());

		assertNotNull(ent2_1.getLastMeeting());
		assertNotNull(ent2_2.getLastMeeting());
	}

	@Test
	public void testEntityJdbc() throws ClassNotFoundException, SQLException {
		String sql = "SELECT id, " + 
				Constants.M_LASTMEETING + ", " +
				"name, " + 
				Constants.M_NEXTMEETING + ", " +
				Constants.I_EXPERT + ", " + 
				Constants.I_WORKPLATFORM + " " +
				"FROM " + Constants.T_TASKFORCE + " ORDER BY id ASC";
		Statement stmt = jdbcConnection.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {

			long id = rs.getLong("id");

			if (id == ent2_1_id.longValue()) {
				assertNotNull(rs.getDate(Constants.M_LASTMEETING));
				assertEquals(TestData.N_ENT2_1, rs.getString("name"));
				assertNotNull(rs.getDate(Constants.M_NEXTMEETING));
				assertEquals((long) ent7_1_id, rs.getLong(Constants.I_EXPERT));
				assertEquals((long) ent1_1_id, rs.getLong(Constants.I_WORKPLATFORM));
			} else if (id == ent2_2_id.longValue()) {
				assertNotNull(rs.getDate(Constants.M_LASTMEETING));
				assertEquals(TestData.N_ENT2_2, rs.getString("name"));
				assertNotNull(rs.getDate(Constants.M_NEXTMEETING));
				assertEquals((long) ent7_2_id, rs.getLong(Constants.I_EXPERT));
				assertEquals((long) ent1_1_id, rs.getLong(Constants.I_WORKPLATFORM));
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

			em.persist(ent1_1);
			em.persist(ent7_1);
			em.persist(ent7_2);
			em.persist(ent2_1);
			em.persist(ent2_2);
			em.persist(ent3_1);
			em.persist(ent3_2);
			em.persist(ent3_3);

			tx.commit();

			ent1_1_id = ent1_1.getId();
			ent7_1_id = ((IPerson) ent7_1).getId();
			ent7_2_id = ((IPerson) ent7_2).getId();
			ent2_1_id = ent2_1.getId();
			ent2_2_id = ent2_2.getId();
			ent3_1_id = ent3_1.getId();
			ent3_2_id = ent3_2.getId();
			ent3_3_id = ent3_3.getId();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));

		}

	}

}
