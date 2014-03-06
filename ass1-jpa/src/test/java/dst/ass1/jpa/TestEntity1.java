package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.dao.IWorkPlatformDAO;
import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class TestEntity1 extends AbstractTest {

	private Long ent1_1_id;

	@Test
	public void testEntity() {
		Session session = (Session) em.getDelegate();
		DAOFactory daoFactory = new DAOFactory(session);
		IWorkPlatformDAO dao = daoFactory.getPlatformDAO();
		
		IWorkPlatform entity = dao.findById(ent1_1_id);

		assertNotNull(entity);
		assertEquals(TestData.N_ENT1_1, entity.getName());
		assertEquals("vienna", entity.getLocation());
		assertTrue((new BigDecimal(20)).compareTo(entity.getCostsPerWorkUnit()) == 0);

		assertNotNull(entity.getTaskForces());
		assertEquals(2, entity.getTaskForces().size());
	}

	@Test
	public void testEntityJdbc() throws ClassNotFoundException, SQLException {
		String sql = "SELECT id, " + Constants.M_COSTSPERWORKUNIT + ", location, name " +
				"FROM " + Constants.T_WORKPLATFORM + " ORDER BY id ASC";
		Statement stmt = jdbcConnection.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		assertTrue(rs.next());

		assertEquals((long) ent1_1_id, rs.getLong("id"));
		assertTrue((new BigDecimal(20.00).compareTo(rs
				.getBigDecimal(Constants.M_COSTSPERWORKUNIT)) == 0));
		assertEquals("vienna", rs.getString("location"));
		assertEquals(TestData.N_ENT1_1, rs.getString("name"));

		assertFalse(rs.next());

		rs.close();
		stmt.close();
	}

	protected void setUpDatabase() {

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			
			IExpert ent7_1 = modelFactory.createExpert();

			ITaskForce ent2_1 = modelFactory.createTaskForce();
			ent2_1.setExpert(ent7_1);
			ent2_1.setName(TestData.N_ENT2_1);
			ent2_1.setLastMeeting(new Date());
			ent2_1.setNextMeeting(new Date());

			ITaskForce ent2_2 = modelFactory.createTaskForce();
			ent2_2.setExpert(ent7_1);
			ent2_2.setName(TestData.N_ENT2_2);
			ent2_2.setLastMeeting(new Date());
			ent2_2.setNextMeeting(new Date());

			IWorkPlatform ent1_1 = modelFactory.createPlatform();
			ent1_1.setName(TestData.N_ENT1_1);
			ent1_1.setLocation("vienna");
			ent1_1.setCostsPerWorkUnit(new BigDecimal(20));

			ent1_1.addTaskForce(ent2_1);
			ent1_1.addTaskForce(ent2_2);

			ent2_1.setWorkPlatform(ent1_1);
			ent2_2.setWorkPlatform(ent1_1);

			em.persist(ent7_1);
			em.persist(ent1_1);
			em.persist(ent2_1);
			em.persist(ent2_2);

			tx.commit();

			ent1_1_id = ent1_1.getId();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}
	}

}
