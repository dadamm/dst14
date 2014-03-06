package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.IPerson;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;

public class TestMembershipEntity extends AbstractTest {

	private Long ent1_1_id;
	private Long user1Id;
	private Long user2Id;

	@Test
	public void testMembership() {
		Session session = (Session) em.getDelegate();
		DAOFactory daoFactory = new DAOFactory(session);

		List<IMembership> memberships = daoFactory.getMembershipDAO().findAll();

		assertNotNull(memberships);
		assertEquals(2, memberships.size());

		assertTrue(checkMembership(user1Id, ent1_1_id, 0.1,
				memberships));
		assertTrue(checkMembership(user2Id, ent1_1_id, 0.1,
				memberships));
	}

	@Test
	public void testMembershipJdbc() throws ClassNotFoundException,
			SQLException {
		String sql = "SELECT discount, registration, " + Constants.I_WORKPLATFORM +
				", user_id FROM " + Constants.T_MEMBERSHIP;
		Statement stmt = jdbcConnection.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {

			long userId = rs.getLong("user_id");

			if (userId == user1Id.longValue()) {
				assertTrue((new Double(0.1))
						.compareTo(rs.getDouble("discount")) == 0);
				assertNotNull(rs.getDate("registration"));
				assertEquals((long) ent1_1_id, rs.getLong(Constants.I_WORKPLATFORM));
				assertEquals((long) user1Id, rs.getLong("user_id"));
			} else if (userId == user2Id.longValue()) {
				assertTrue((new Double(0.1))
						.compareTo(rs.getDouble("discount")) == 0);
				assertNotNull(rs.getDate("registration"));
				assertEquals((long) ent1_1_id, rs.getLong(Constants.I_WORKPLATFORM));
				assertEquals((long) user2Id, rs.getLong("user_id"));
			} else {
				fail("Unexpected Membership found!");
			}

		}

		rs.close();
		stmt.close();

	}

	protected void setUpDatabase() {

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			IWorkPlatform ent1_1 = modelFactory.createPlatform();
			IUser user1 = modelFactory.createUser();
			IUser user2 = modelFactory.createUser();
			user1.setUsername("u1");
			user2.setUsername("u2");

			IMembership membership1 = modelFactory.createMembership();
			membership1.setDiscount(0.10);
			membership1.setRegistration(new Date());

			IMembershipKey key1 = modelFactory.createMembershipKey();
			key1.setUser(user1);
			key1.setWorkPlatform(ent1_1);

			membership1.setId(key1);
			user1.addMembership(membership1);

			IMembership membership2 = modelFactory.createMembership();
			membership2.setDiscount(0.10);
			membership2.setRegistration(new Date());

			IMembershipKey key2 = modelFactory.createMembershipKey();
			key2.setUser(user2);
			key2.setWorkPlatform(ent1_1);

			membership2.setId(key2);
			user2.addMembership(membership2);

			em.persist(ent1_1);
			em.persist(user1);
			em.persist(user2);
			em.persist(membership1);
			em.persist(membership2);

			tx.commit();

			ent1_1_id = ent1_1.getId();
			user1Id = ((IPerson) user1).getId();
			user2Id = ((IPerson) user2).getId();
		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
