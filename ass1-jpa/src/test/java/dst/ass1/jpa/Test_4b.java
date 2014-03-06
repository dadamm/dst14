package dst.ass1.jpa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class Test_4b extends AbstractTest {

	private Long ent3_1_id;

	@Test
	public void testEntityListener() {
		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {

			ITaskWorker ent3_1 = daoFactory.getTaskWorkerDAO().findById(
					ent3_1_id);
			assertNotNull(ent3_1);

			ent3_1.setLocation("AUT-VIE@1160");
			Date lastUpdate = ent3_1.getLastTraining();
			try {
				// for Testing
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			em.flush();

			assertTrue(ent3_1.getLastTraining().after(lastUpdate));

			tx.rollback();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}
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

			ITaskForce ent2_1 = modelFactory.createTaskForce();
			ent2_1.setExpert(ent7_1);
			ent2_1.setName(TestData.N_ENT2_1);
			ent2_1.setLastMeeting(new Date());
			ent2_1.setNextMeeting(new Date());

			ent7_1.addAdvisedTaskForce(ent2_1);

			ent2_1.addTaskWorker(ent3_1);
			ent3_1.setTaskForce(ent2_1);

			IWorkPlatform ent1_1 = modelFactory.createPlatform();
			ent2_1.setWorkPlatform(ent1_1);

			em.persist(ent1_1);
			em.persist(ent7_1);
			em.persist(ent2_1);
			em.persist(ent3_1);

			tx.commit();

			ent3_1_id = ent3_1.getId();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
