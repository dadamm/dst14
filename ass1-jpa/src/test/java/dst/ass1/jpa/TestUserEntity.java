package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IAddress;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.IPerson;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.JdbcHelper;

public class TestUserEntity extends AbstractTest {

	private Long user1Id;
	private Long user2Id;
	private Long ent5_1Id;
	private Long ent5_2Id;
	private Long ent5_3Id;

	@Test
	public void testUser() throws NoSuchAlgorithmException {
		Session session = (Session) em.getDelegate();
		DAOFactory daoFactory = new DAOFactory(session);

		List<IUser> users = daoFactory.getUserDAO().findAll();

		assertNotNull(users);
		assertEquals(2, users.size());

		IUser u1 = daoFactory.getUserDAO().findById(user1Id);
		IUser u2 = daoFactory.getUserDAO().findById(user2Id);
		IPerson p1 = (IPerson) u1;
		IPerson p2 = (IPerson) u2;
		MessageDigest md = MessageDigest.getInstance("MD5");

		assertEquals("account1", u1.getAccountNo());
		assertEquals("bank1", u1.getBankCode());
		assertEquals(dst.ass1.jpa.util.test.TestData.N_USER_1, u1.getUsername());
		assertEquals("city1", p1.getAddress().getCity());
		assertEquals("street1", p1.getAddress().getStreet());
		assertEquals("zip1", p1.getAddress().getZipCode());
		assertEquals("first1", p1.getFirstName());
		assertEquals("last1", p1.getLastName());
		assertEquals(user1Id, p1.getId());

		byte[] passwd1 = md.digest("pw1".getBytes());
		assertTrue(Arrays.equals(passwd1, u1.getPassword()));

		assertNotNull(u1.getMemberships());
		assertEquals(1, u1.getMemberships().size());
		assertEquals(p1.getId(), ((IPerson) u1.getMemberships().get(0).getId()
				.getUser()).getId());

		assertNotNull(u1.getTasks());
		assertEquals(2, u1.getTasks().size());

		List<Long> ids = getTaskIds(u1.getTasks());
		assertTrue(ids.contains(ent5_1Id));
		assertTrue(ids.contains(ent5_3Id));

		assertEquals("account2", u2.getAccountNo());
		assertEquals("bank2", u2.getBankCode());
		assertEquals(dst.ass1.jpa.util.test.TestData.N_USER_2, u2.getUsername());
		assertEquals("city2", p2.getAddress().getCity());
		assertEquals("street2", p2.getAddress().getStreet());
		assertEquals("zip2", p2.getAddress().getZipCode());
		assertEquals("first2", p2.getFirstName());
		assertEquals("last2", p2.getLastName());
		assertEquals(user2Id, p2.getId());

		byte[] passwd2 = md.digest("pw2".getBytes());
		assertTrue(Arrays.equals(passwd2, u2.getPassword()));

		assertNotNull(u2.getMemberships());
		assertEquals(1, u2.getMemberships().size());
		assertEquals(p2.getId(), ((IPerson) u2.getMemberships().get(0).getId()
				.getUser()).getId());

		assertNotNull(u2.getTasks());
		assertEquals(1, u2.getTasks().size());
		assertEquals(ent5_2Id, u2.getTasks().get(0).getId());

	}

	@Test
	public void testUserJdbc() throws Exception {
		int type = JdbcHelper.getInheritanceType(jdbcConnection, "Person");
		String sql = "";
		switch (type) {
		case 0:
			sql = "SELECT u.id, p.city, p.street, p.zipCode, p.firstName, p.lastName, u.accountNo, u.bankCode, u.password, u.username FROM User u, Person p WHERE u.id=p.id ORDER BY u.id ASC";
			break;
		case 1:
			sql = "SELECT id, city, street, zipCode, firstName, lastName, accountNo, bankCode, password, username FROM User ORDER BY id ASC";
			break;
		default:
			throw new Exception("unknown inheritance type");
		}

		Statement stmt = jdbcConnection.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {

			long id = rs.getLong("id");

			if (id == user1Id.longValue()) {
				assertEquals((long) user1Id, rs.getLong("id"));
				assertEquals("city1", rs.getString("city"));
				assertEquals("street1", rs.getString("street"));
				assertEquals("zip1", rs.getString("zipCode"));
				assertEquals("first1", rs.getString("firstName"));
				assertEquals("last1", rs.getString("lastName"));
				assertEquals("account1", rs.getString("accountNo"));
				assertEquals("bank1", rs.getString("bankCode"));
				assertEquals(dst.ass1.jpa.util.test.TestData.N_USER_1, rs.getString("username"));
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] passwd = md.digest(("pw1").getBytes());
				assertTrue(Arrays.equals(passwd, rs.getBytes("password")));
			} else if (id == user2Id.longValue()) {
				assertEquals((long) user2Id, rs.getLong("id"));
				assertEquals("city2", rs.getString("city"));
				assertEquals("street2", rs.getString("street"));
				assertEquals("zip2", rs.getString("zipCode"));
				assertEquals("first2", rs.getString("firstName"));
				assertEquals("last2", rs.getString("lastName"));
				assertEquals("account2", rs.getString("accountNo"));
				assertEquals("bank2", rs.getString("bankCode"));
				assertEquals(dst.ass1.jpa.util.test.TestData.N_USER_2, rs.getString("username"));
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] passwd = md.digest(("pw2").getBytes());
				assertTrue(Arrays.equals(passwd, rs.getBytes("password")));
			} else {
				fail("Unexpected User found!");
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

			IAddress address3 = modelFactory.createAddress();
			IAddress address4 = modelFactory.createAddress();

			address3.setCity("city1");
			address3.setStreet("street1");
			address3.setZipCode("zip1");

			address4.setCity("city2");
			address4.setStreet("street2");
			address4.setZipCode("zip2");

			MessageDigest md = MessageDigest.getInstance("MD5");

			IUser user1 = modelFactory.createUser();
			((IPerson) user1).setFirstName("first1");
			((IPerson) user1).setLastName("last1");
			((IPerson) user1).setAddress(address3);
			user1.setAccountNo("account1");
			user1.setUsername(dst.ass1.jpa.util.test.TestData.N_USER_1);
			user1.setBankCode("bank1");
			user1.setPassword(md.digest("pw1".getBytes()));

			IUser user2 = modelFactory.createUser();
			((IPerson) user2).setFirstName("first2");
			((IPerson) user2).setLastName("last2");
			((IPerson) user2).setAddress(address4);
			user2.setAccountNo("account2");
			user2.setUsername(dst.ass1.jpa.util.test.TestData.N_USER_2);
			user2.setBankCode("bank2");
			user2.setPassword(md.digest("pw2".getBytes()));

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

			IMetadata ent6_1 = modelFactory.createMetadata();
			IMetadata ent6_2 = modelFactory.createMetadata();
			IMetadata ent6_3 = modelFactory.createMetadata();

			em.persist(ent6_1);
			em.persist(ent6_2);
			em.persist(ent6_3);

			ITaskProcessing ent4_1 = modelFactory.createTaskProcessing();
			ITaskProcessing ent4_2 = modelFactory.createTaskProcessing();
			ITaskProcessing ent4_3 = modelFactory.createTaskProcessing();

			ITask ent5_1 = modelFactory.createTask();
			ent5_1.setAssignedWorkUnits(2);
			ent5_1.setProcessingTime(0);
			ent5_1.setMetadata(ent6_1);
			ent5_1.setTaskProcessing(ent4_1);
			ent5_1.setUser(user1);
			user1.addTask(ent5_1);

			ITask ent5_2 = modelFactory.createTask();
			ent5_2.setAssignedWorkUnits(3);
			ent5_2.setProcessingTime(0);
			ent5_2.setMetadata(ent6_2);
			ent5_2.setTaskProcessing(ent4_2);
			ent5_2.setUser(user2);
			user2.addTask(ent5_2);

			ITask ent5_3 = modelFactory.createTask();
			ent5_3.setAssignedWorkUnits(4);
			ent5_3.setProcessingTime(0);
			ent5_3.setMetadata(ent6_3);
			ent5_3.setTaskProcessing(ent4_3);
			ent5_3.setUser(user1);
			user1.addTask(ent5_3);

			em.persist(ent4_1);
			em.persist(ent4_2);
			em.persist(ent4_3);

			em.persist(ent1_1);
			em.persist(user1);
			em.persist(user2);
			em.persist(ent5_1);
			em.persist(ent5_2);
			em.persist(ent5_3);
			em.persist(membership1);
			em.persist(membership2);

			tx.commit();

			user1Id = ((IPerson) user1).getId();
			user2Id = ((IPerson) user2).getId();
			ent5_1Id = ent5_1.getId();
			ent5_2Id = ent5_2.getId();
			ent5_3Id = ent5_3.getId();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
