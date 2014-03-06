package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.TaskStatus;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class Test_2c02 extends AbstractTest {

	private TestData testData;

	@Before
	public void setUp() throws NoSuchAlgorithmException {
		testData = new TestData(em);
		testData.insertTestData();
	}

	@Test
	public void testFindTasksForStatusFinishedStartandFinish1() {
		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());

		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();

			List<ITask> tasks = daoFactory.getTaskDAO()
					.findTasksForStatusFinishedStartandFinish(null, null);
			boolean isNoTasks = tasks == null || tasks.size() == 0;
			assertTrue(isNoTasks);
		} finally {
			if (tx != null) {
				tx.rollback();
			}
		}
	}

	@Test
	public void testFindTasksForStatusFinishedStartandFinish2() {
		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());

		try {
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			ITask task = daoFactory.getTaskDAO().findById(testData.entity5_1_Id);
			task.getTaskProcessing().setStatus(TaskStatus.FINISHED);

			em.persist(task);

			tx.commit();

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}

		List<ITask> tasks = daoFactory.getTaskDAO()
				.findTasksForStatusFinishedStartandFinish(null, null);
		assertNotNull(tasks);
		assertEquals(1, tasks.size());
		assertEquals(testData.entity5_1_Id, tasks.get(0).getId());
	}

	@Test
	public void testFindTasksForStatusFinishedStartandFinish3() {
		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());

		try {
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			ITask task = daoFactory.getTaskDAO().findById(testData.entity5_1_Id);
			task.getTaskProcessing().setStatus(TaskStatus.FINISHED);
			task.getTaskProcessing().setStart(createDate(2012, 1, 20));
			task.getTaskProcessing().setEnd(createDate(2012, 11, 30));

			em.persist(task);

			tx.commit();

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}

		List<ITask> tasks = daoFactory.getTaskDAO()
				.findTasksForStatusFinishedStartandFinish(createDate(2012, 1, 1),
						createDate(2012, 12, 31));
		assertNotNull(tasks);
		assertEquals(0, tasks.size());

		tasks = daoFactory.getTaskDAO().findTasksForStatusFinishedStartandFinish(
				createDate(2012, 1, 1), createDate(2012, 10, 1));
		assertNotNull(tasks);
		assertEquals(0, tasks.size());

		tasks = daoFactory.getTaskDAO().findTasksForStatusFinishedStartandFinish(
				createDate(2012, 10, 1), createDate(2012, 12, 1));
		assertNotNull(tasks);
		assertEquals(0, tasks.size());

		tasks = daoFactory.getTaskDAO().findTasksForStatusFinishedStartandFinish(
				createDate(2012, 1, 20), createDate(2012, 11, 30));
		assertEquals(1, tasks.size());
		assertEquals(testData.entity5_1_Id, tasks.get(0).getId());

		tasks = daoFactory.getTaskDAO().findTasksForStatusFinishedStartandFinish(
				createDate(2012, 1, 20), null);
		assertEquals(1, tasks.size());
		assertEquals(testData.entity5_1_Id, tasks.get(0).getId());

		tasks = daoFactory.getTaskDAO().findTasksForStatusFinishedStartandFinish(
				null, createDate(2012, 11, 30));
		assertEquals(1, tasks.size());
		assertEquals(testData.entity5_1_Id, tasks.get(0).getId());

	}

	private Date createDate(int year, int month, int day) {

		String temp = year + "/" + month + "/" + day;
		Date date = null;

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			date = formatter.parse(temp);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;

	}

}
