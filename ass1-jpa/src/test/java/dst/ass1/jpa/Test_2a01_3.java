package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.TaskStatus;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class Test_2a01_3 extends AbstractTest {

	private TestData testData;

	@Before
	public void setUp() throws NoSuchAlgorithmException {
		testData = new TestData(em);
		testData.insertTestData();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNamedQueryFindAllFinishedTasks1() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em
					.createNamedQuery(Constants.Q_ALLFINISHEDTASKS);

			List<ITask> result = (List<ITask>) query.getResultList();
			assertNotNull(result);
			assertEquals(0, result.size());

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));

		} finally {
			tx.rollback();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNamedQueryFindAllFinishedTasks2() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			
			DAOFactory fac = new DAOFactory((Session)em.getDelegate());
			ITask task = fac.getTaskDAO().findById(testData.entity5_1_Id);
			task.getTaskProcessing().setStatus(TaskStatus.FINISHED);

			Query query = em
					.createNamedQuery(Constants.Q_ALLFINISHEDTASKS);

			List<ITask> result = (List<ITask>) query.getResultList();
			assertNotNull(result);
			assertEquals(1, result.size());

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));

		} finally {
			tx.rollback();
		}
	}

}
