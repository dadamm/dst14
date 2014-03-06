package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.dao.ITaskDAO;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class Test_2c01 extends AbstractTest {

	private TestData testData;

	@Before
	public void setUp() throws NoSuchAlgorithmException {
		testData = new TestData(em);
		testData.insertTestData();
	}

	@Test
	public void findTasksForUserAndContext1() {
		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());

		ITaskDAO ent5_Dao = daoFactory.getTaskDAO();
		List<ITask> ent5_list = ent5_Dao.findTasksForUserAndContext(
				dst.ass1.jpa.util.test.TestData.N_USER_1, TestData.N_ENT6_1);
		assertNotNull(ent5_list);
		assertEquals(1, ent5_list.size());

		List<Long> ids = getTaskIds(ent5_list);
		assertTrue(ids.contains(testData.entity5_1_Id));
	}

	@Test
	public void findTasksForUserAndContext2() {
		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());

		ITaskDAO ent5_Dao = daoFactory.getTaskDAO();
		List<ITask> ent5_list = ent5_Dao.findTasksForUserAndContext(
				dst.ass1.jpa.util.test.TestData.N_USER_1, "invalid_context");
		assertNotNull(ent5_list);
		assertEquals(0, ent5_list.size());
	}

	@Test
	public void findTasksForUserAndContext3() {
		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());

		ITaskDAO ent5_Dao = daoFactory.getTaskDAO();
		List<ITask> ent5_list = ent5_Dao.findTasksForUserAndContext(
				dst.ass1.jpa.util.test.TestData.N_USER_2, TestData.N_ENT6_1);
		assertNotNull(ent5_list);
		assertEquals(0, ent5_list.size());
	}

	@Test
	public void findTasksForUserAndContext4() {
		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());

		ITaskDAO ent5_Dao = daoFactory.getTaskDAO();
		List<ITask> ent5_list = ent5_Dao.findTasksForUserAndContext(
				dst.ass1.jpa.util.test.TestData.N_USER_2, null);
		assertNotNull(ent5_list);
		assertEquals(1, ent5_list.size());

		List<Long> ids = getTaskIds(ent5_list);
		assertTrue(ids.contains(testData.entity5_2_Id));
	}

	@Test
	public void findTasksForUserAndContext5() {
		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());

		ITaskDAO ent5_Dao = daoFactory.getTaskDAO();
		List<ITask> ent5_list = ent5_Dao.findTasksForUserAndContext("user5",
				TestData.N_ENT6_1);
		assertNotNull(ent5_list);
		assertEquals(0, ent5_list.size());
	}

	@Test
	public void findTasksForUserAndContext6() {
		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());

		ITaskDAO ent5_Dao = daoFactory.getTaskDAO();
		List<ITask> ent5_list = ent5_Dao.findTasksForUserAndContext(null,
				TestData.N_ENT6_1);
		assertNotNull(ent5_list);
		assertEquals(1, ent5_list.size());

		List<Long> ids = getTaskIds(ent5_list);
		assertTrue(ids.contains(testData.entity5_1_Id));
	}

	@Test
	public void findTasksForUserAndContext7() {
		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());

		try {
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			ITask ent5 = daoFactory.getTaskDAO().findById(testData.entity5_3_Id);
			ent5.getMetadata().setContext(TestData.N_ENT6_1);

			em.persist(ent5);

			tx.commit();

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}

		ITaskDAO ent5_Dao = daoFactory.getTaskDAO();
		List<ITask> ent5 = ent5_Dao.findTasksForUserAndContext(
				dst.ass1.jpa.util.test.TestData.N_USER_1, TestData.N_ENT6_1);
		assertNotNull(ent5);
		assertEquals(2, ent5.size());

		List<Long> ids = getTaskIds(ent5);
		assertTrue(ids.contains(testData.entity5_1_Id));
		assertTrue(ids.contains(testData.entity5_3_Id));
	}

	@Test
	public void findTasksForUserAndContext8() {
		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());

		ITaskDAO ent5_Dao = daoFactory.getTaskDAO();
		List<ITask> ent5_list = ent5_Dao.findTasksForUserAndContext(null, null);
		assertNotNull(ent5_list);
		assertEquals(4, ent5_list.size());

		List<Long> ids = getTaskIds(ent5_list);
		assertTrue(ids.contains(testData.entity5_1_Id));
		assertTrue(ids.contains(testData.entity5_2_Id));
		assertTrue(ids.contains(testData.entity5_3_Id));
	}
}
