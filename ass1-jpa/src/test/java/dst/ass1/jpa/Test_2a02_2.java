package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.ModelFactory;
import dst.ass1.jpa.model.TaskStatus;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class Test_2a02_2 extends AbstractTest {

	private TestData testData;

	@Before
	public void setUp() throws NoSuchAlgorithmException {
		testData = new TestData(em);
		testData.insertTestData();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testQuery() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());
			ModelFactory modelFactory = new ModelFactory();

			IUser u2 = daoFactory.getUserDAO().findById(testData.user2Id);
			assertNotNull(u2);

			IMetadata ent6_1 = modelFactory.createMetadata();
			ent6_1.setContext("ctx_env");
			ent6_1.addSetting("param");
			em.persist(ent6_1);

			ITaskProcessing ent4_1 = modelFactory.createTaskProcessing();
			ent4_1.setStart(new Date(System.currentTimeMillis() - 18000000));
			ent4_1.setEnd(new Date());
			ent4_1.setStatus(TaskStatus.SCHEDULED);

			ITaskWorker ent3_1 = daoFactory.getTaskWorkerDAO().findById(
					testData.entity3_1_Id);
			assertNotNull(ent3_1);
			ent4_1.addWorker(ent3_1);
			ent3_1.addTaskProcessing(ent4_1);

			ITask ent5 = modelFactory.createTask();
			ent5.setAssignedWorkUnits(2);
			ent5.setProcessingTime(0);
			ent5.setMetadata(ent6_1);
			ent5.setTaskProcessing(ent4_1);
			ent5.setUser(u2);
			u2.addTask(ent5);

			em.persist(ent4_1);
			em.persist(ent3_1);
			em.persist(ent5);
			em.persist(u2);

			tx.commit();

			Query query = em.createNamedQuery(Constants.Q_MOSTACTIVEUSER);

			List<IUser> result = (List<IUser>) query.getResultList();
			assertNotNull(result);
			assertEquals(1, result.size());

			List<Long> ids = getUserIds(result);

			assertTrue(ids.contains(testData.user1Id));

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}

	}
}
