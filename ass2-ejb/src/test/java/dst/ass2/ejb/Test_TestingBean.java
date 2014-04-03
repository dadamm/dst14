package dst.ass2.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dst.ass1.jpa.util.test.TestData;
import dst.ass2.AbstractEJBTest;

public class Test_TestingBean extends AbstractEJBTest {

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testAdmin() {
		assertEquals(1, jdbcTestUtil.countEntity7_FROM_DB("city3",
				"street3", "1160", "Sepp", "Huber"));
	}

	@Test
	public void testEntity2() {
		assertEquals(1, jdbcTestUtil.countEntity2_FROM_DB("cl1"));
		assertEquals(1, jdbcTestUtil.countEntity2_FROM_DB("cl2"));
	}

	@Test
	public void testEntity3() {
		assertEquals(1, jdbcTestUtil.coungEntity3_FROM_DB(4,
				"AUT-XYZ@5678", "longername1"));
		assertEquals(1, jdbcTestUtil.coungEntity3_FROM_DB(6,
				"AUT-XYZ@1234", "longername2"));
		assertEquals(1, jdbcTestUtil.coungEntity3_FROM_DB(8,
				"AUT-XYZ@1234", "longername3"));
		assertEquals(1, jdbcTestUtil.coungEntity3_FROM_DB(4,
				"AUT-XYZ@5678", "longername4"));
		assertEquals(1, jdbcTestUtil.coungEntity3_FROM_DB(8,
				"AUT-XYZ@5678", "longername5"));
	}

	@Test
	public void testEntity6() {
		assertEquals(1,
				jdbcTestUtil.countEntity6_FROM_DB("workflow1"));
		assertEquals(1,
				jdbcTestUtil.countEntity6_FROM_DB("workflow2"));
	}

	@Test
	public void testEntity4() {
		assertEquals(1, jdbcTestUtil.countEntity4_FROM_DB());
	}

	@Test
	public void testEntity1() {
		assertEquals(1, jdbcTestUtil.countEntity1_FROM_DB(
				BigDecimal.valueOf(5.00), "location1", TestData.N_ENT1_1));
		assertEquals(1, jdbcTestUtil.countEntity1_FROM_DB(
				BigDecimal.valueOf(7.00), "location2", TestData.N_ENT1_2));
	}

	@Test
	public void testEntity5() {
		assertEquals(1, jdbcTestUtil.countEntity5_FROM_DB(false));
	}

	@Test
	public void testMembership() {
		assertEquals(1, jdbcTestUtil.countMemberships_FROM_DB(Double
				.valueOf(0.1)));
		assertEquals(1, jdbcTestUtil.countMemberships_FROM_DB(Double
				.valueOf(0.2)));
		assertEquals(1, jdbcTestUtil.countMemberships_FROM_DB(Double
				.valueOf(0.3)));
	}

	@Test
	public void testUser() {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			assertEquals(1, jdbcTestUtil.countUsers_FROM_DB("city1",
					"street1", "1140", "Hans", "Mueller", "111111", "1111",
					"hansi", md.digest(("pw").getBytes())));

			assertEquals(1, jdbcTestUtil.countUsers_FROM_DB("city2",
					"street2", "1150", "Franz", "Mueller", "222222", "1111",
					"franz", md.digest(("liebe").getBytes())));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		}
	}
}
