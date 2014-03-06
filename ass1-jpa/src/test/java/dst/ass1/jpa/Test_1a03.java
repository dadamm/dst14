package dst.ass1.jpa;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.JdbcHelper;
import dst.ass1.jpa.util.test.TestData;

public class Test_1a03 extends AbstractTest {

	private Long ent2_1_id;
	
	@Test
	public void testEntity2NameConstraint() {
		boolean isConstraint = false;
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			// Entity #2
			ITaskForce ent2_2 = (new DAOFactory((Session) em.getDelegate()))
					.getTaskForceDAO().findById(ent2_1_id);
			ent2_2.setName(TestData.N_ENT2_2);
			em.persist(ent2_2);
			em.flush();

		} catch (PersistenceException e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				isConstraint = true;
			}

		} finally {
			tx.rollback();
		}

		assertTrue(isConstraint);
	}

	@Test
	public void testEntity2NameConstraintJdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(JdbcHelper.isIndex(Constants.T_TASKFORCE, "name", false,
				jdbcConnection));
	}

	protected void setUpDatabase() {
		ITaskForce ent2_1 = modelFactory.createTaskForce();
		ITaskForce ent2_2 = modelFactory.createTaskForce();
		IExpert en7_1 = modelFactory.createExpert();
		IWorkPlatform ent1_1 = modelFactory.createPlatform();

		ent2_1.setExpert(en7_1);
		ent2_1.setName(TestData.N_ENT2_1);
		ent2_1.setWorkPlatform(ent1_1);

		ent2_2.setName(TestData.N_ENT2_2);
		ent2_2.setExpert(en7_1);
		ent2_2.setWorkPlatform(ent1_1);

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(en7_1);
			em.persist(ent1_1);
			em.persist(ent2_1);
			em.persist(ent2_2);
			tx.commit();
			
			ent2_1_id = ent2_1.getId();
		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}
	}

}
