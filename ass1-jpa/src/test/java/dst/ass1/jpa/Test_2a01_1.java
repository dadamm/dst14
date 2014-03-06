package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class Test_2a01_1 extends AbstractTest {

	private TestData testData;

	@Before
	public void setUp() throws NoSuchAlgorithmException {
		testData = new TestData(em);
		testData.insertTestData();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNamedQueryFindUsersWithActiveMembership1() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em
					.createNamedQuery(Constants.Q_USERSWITHACTIVEMEMBERSHIP);
			query.setParameter("name", TestData.N_ENT1_1);
			query.setParameter("minNr", 2L);

			List<IUser> result = (List<IUser>) query.getResultList();
			assertNotNull(result);
			assertEquals(1, result.size());

			assertTrue(getUserIds(result).contains(testData.user1Id));

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));

		} finally {
			tx.rollback();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNamedQueryFindUsersWithActiveMembership2() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em
					.createNamedQuery(Constants.Q_USERSWITHACTIVEMEMBERSHIP);
			query.setParameter("name", "foobar");
			query.setParameter("minNr", 2L);

			List<IUser> result = (List<IUser>) query.getResultList();
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
	public void testNamedQueryFindUsersWithActiveMembership3() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em
					.createNamedQuery(Constants.Q_USERSWITHACTIVEMEMBERSHIP);
			query.setParameter("name", TestData.N_ENT1_1);
			query.setParameter("minNr", 0L);

			List<IUser> result = (List<IUser>) query.getResultList();
			assertNotNull(result);
			assertEquals(2, result.size());

			List<Long> ids = getUserIds(result);
			assertTrue(ids.contains(testData.user1Id));
			assertTrue(ids.contains(testData.user2Id));

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));

		} finally {
			tx.rollback();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNamedQueryFindUsersWithActiveMembership4() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em
					.createNamedQuery(Constants.Q_USERSWITHACTIVEMEMBERSHIP);
			query.setParameter("name", TestData.N_ENT1_1);
			query.setParameter("minNr", 1L);

			List<IUser> result = (List<IUser>) query.getResultList();
			assertNotNull(result);
			assertEquals(2, result.size());

			List<Long> ids = getUserIds(result);
			assertTrue(ids.contains(testData.user1Id));
			assertTrue(ids.contains(testData.user2Id));

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));

		} finally {
			tx.rollback();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNamedQueryFindUsersWithActiveMembership5() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em
					.createNamedQuery(Constants.Q_USERSWITHACTIVEMEMBERSHIP);
			query.setParameter("name", TestData.N_ENT1_1);
			query.setParameter("minNr", 3L);

			List<IUser> result = (List<IUser>) query.getResultList();
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
	public void testNamedQueryFindUsersWithActiveMembership6() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em
					.createNamedQuery(Constants.Q_USERSWITHACTIVEMEMBERSHIP);
			query.setParameter("name", "invalidName");
			query.setParameter("minNr", 2L);

			List<IUser> result = (List<IUser>) query.getResultList();
			assertNotNull(result);
			assertEquals(0, result.size());

		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));

		} finally {
			tx.rollback();
		}
	}
}
