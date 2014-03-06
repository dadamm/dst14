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
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.JdbcHelper;
import dst.ass1.jpa.util.test.TestData;

public class Test_1a04 extends AbstractTest {

	private Long ent_3_1_id;

	@Test
	public void testEntity3Constraint() {
		boolean isConstraint = false;
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			// Entity #3
			ITaskWorker ent3_1 = (new DAOFactory((Session) em.getDelegate()))
					.getTaskWorkerDAO().findById(ent_3_1_id);
			ent3_1.setName(TestData.N_ENT3_2);
			em.persist(ent3_1);
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
	public void testEntity3ConstraintJdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(JdbcHelper.isIndex(Constants.T_TASKWORKER, "name", false, jdbcConnection));
	}

	public void setUpDatabase() {

		ITaskForce ent2_1 = modelFactory.createTaskForce();
		IExpert ent7_1 = modelFactory.createExpert();
		IWorkPlatform ent1_1 = modelFactory.createPlatform();
		ent2_1.setExpert(ent7_1);
		ent2_1.setName(TestData.N_ENT2_1);
		ent2_1.setWorkPlatform(ent1_1);

		ITaskWorker ent3_1 = modelFactory.createTaskWorker();
		ent3_1.setName(TestData.N_ENT3_1);
		ent3_1.setTaskForce(ent2_1);

		ITaskWorker ent3_2 = modelFactory.createTaskWorker();
		ent3_2.setName(TestData.N_ENT3_2);
		ent3_2.setTaskForce(ent2_1);

		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(ent1_1);
			em.persist(ent7_1);
			em.persist(ent2_1);
			em.persist(ent3_1);
			em.persist(ent3_2);
			tx.commit();

			ent_3_1_id = ent3_1.getId();
		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}

	}

}
