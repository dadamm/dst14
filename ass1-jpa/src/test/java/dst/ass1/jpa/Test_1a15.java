package dst.ass1.jpa;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.util.test.TestData;

public class Test_1a15 extends AbstractTest {

	private Long ent2_1Id;
	private Long ent2_2Id;

	public void testEntity2Association() {
		Session session = (Session) em.getDelegate();
		DAOFactory daoFactory = new DAOFactory(session);

		ITaskForce ent2_1 = daoFactory.getTaskForceDAO().findById(ent2_1Id);
		assertNotNull(ent2_1.getComposedOf());
		assertEquals(1, ent2_1.getComposedOf().size());
		assertEquals(ent2_2Id, ent2_1.getComposedOf().get(0).getId());
	}

	@Test
	public void testEntity2ComposedJdbc() throws ClassNotFoundException,
			SQLException {
		String sql = "SELECT partOf_id, composedOf_id from composed_of";
		Statement stmt = jdbcConnection.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		assertTrue(rs.next());

		assertEquals((long) ent2_1Id, rs.getLong("partOf_id"));
		assertEquals((long) ent2_2Id, rs.getLong("composedOf_id"));

		assertFalse(rs.next());

		rs.close();
	}

	public void setUpDatabase() {

		EntityTransaction tx = em.getTransaction();
		try {

			tx.begin();

			IExpert ent7 = modelFactory.createExpert();
			IWorkPlatform ent1_1 = modelFactory.createPlatform();

			em.persist(ent7);
			em.persist(ent1_1);

			ITaskForce ent2_1 = modelFactory.createTaskForce();
			ent2_1.setExpert(ent7);
			ent2_1.setWorkPlatform(ent1_1);
			ent2_1.setName(TestData.N_ENT2_1);
			ent2_1.setLastMeeting(new Date());
			ent2_1.setNextMeeting(new Date());

			ITaskForce ent2_2 = modelFactory.createTaskForce();
			ent2_2.setExpert(ent7);
			ent2_2.setWorkPlatform(ent1_1);
			ent2_2.setName(TestData.N_ENT2_2);
			ent2_2.setLastMeeting(new Date());
			ent2_2.setNextMeeting(new Date());

			ent2_1.addComposedOf(ent2_2);
			ent2_2.addPartOf(ent2_1);

			em.persist(ent2_1);
			em.persist(ent2_2);

			tx.commit();

			ent2_1Id = ent2_1.getId();
			ent2_2Id = ent2_2.getId();

		} catch (Exception e) {
			tx.rollback();
			fail(e.getMessage());
		}
	}
}
