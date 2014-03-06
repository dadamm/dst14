package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IAddress;
import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.IPerson;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.JdbcHelper;
import dst.ass1.jpa.util.test.TestData;

public class TestEntity7 extends AbstractTest {

	private Long ent7_1Id;
	private Long ent7_Id;
	private Long ent2_1Id;
	private Long ent2_2Id;

	@Test
	public void testEntity() {
		Session session = (Session) em.getDelegate();
		DAOFactory daoFactory = new DAOFactory(session);

		List<IExpert> list = daoFactory.getExpertDAO().findAll();

		assertNotNull(list);
		assertEquals(2, list.size());

		IExpert ent7_1 = daoFactory.getExpertDAO().findById(ent7_1Id);
		IExpert ent7_2 = daoFactory.getExpertDAO().findById(ent7_Id);

		IPerson p1 = (IPerson) ent7_1;
		IPerson p2 = (IPerson) ent7_2;

		assertEquals(ent7_1Id, p1.getId());
		assertEquals("city1", p1.getAddress().getCity());
		assertEquals("street1", p1.getAddress().getStreet());
		assertEquals("zip1", p1.getAddress().getZipCode());
		assertEquals(TestData.N_ENT7_1, p1.getFirstName());
		assertEquals(TestData.N_ENT7_1, p1.getLastName());

		assertNotNull(ent7_1.getAdvisedTaskForces());
		assertEquals(1, ent7_1.getAdvisedTaskForces().size());
		assertEquals(ent2_1Id, ent7_1.getAdvisedTaskForces().get(0).getId());

		assertEquals(ent7_Id, p2.getId());
		assertEquals("city2", p2.getAddress().getCity());
		assertEquals("street2", p2.getAddress().getStreet());
		assertEquals("zip2", p2.getAddress().getZipCode());
		assertEquals(TestData.N_ENT7_2, p2.getFirstName());
		assertEquals(TestData.N_ENT7_2, p2.getLastName());

		assertNotNull(ent7_2.getAdvisedTaskForces());
		assertEquals(1, ent7_2.getAdvisedTaskForces().size());
		assertEquals(ent2_2Id, ent7_2.getAdvisedTaskForces().get(0).getId());

	}

	@Test
	public void testEntityJdbc() throws Exception {
		int type = JdbcHelper.getInheritanceType(jdbcConnection, "Person");
		String sql = "";
		switch (type) {
		case 0:
			sql = 	"SELECT e.id, p.city, p.street, p.zipCode, p.firstName, p.lastName FROM " +
					Constants.T_EXPERT + " e, " +
					"Person p " +
					"WHERE e.id = p.id ORDER BY e.id ASC";
			break;
		case 1:
			sql = 	"SELECT id, city, street, zipCode, firstName, lastName FROM " +
					Constants.T_EXPERT + " ORDER BY id ASC";
			break;
		default:
			throw new Exception("unknown inheritance type");
		}

		Statement stmt = jdbcConnection.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {

			long id = rs.getLong("id");

			if (id == ent7_1Id.longValue()) {
				assertEquals("city1", rs.getString("city"));
				assertEquals("street1", rs.getString("street"));
				assertEquals("zip1", rs.getString("zipCode"));
				assertEquals(TestData.N_ENT7_1, rs.getString("firstName"));
				assertEquals(TestData.N_ENT7_1, rs.getString("lastName"));
			} else if (id == ent7_Id.longValue()) {
				assertEquals("city2", rs.getString("city"));
				assertEquals("street2", rs.getString("street"));
				assertEquals("zip2", rs.getString("zipCode"));
				assertEquals(TestData.N_ENT7_2, rs.getString("firstName"));
				assertEquals(TestData.N_ENT7_2, rs.getString("lastName"));
			} else {
				fail("Unexpected entity found!");
			}

		}

		rs.close();
		stmt.close();
	}

	protected void setUpDatabase() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			IAddress address1 = modelFactory.createAddress();
			IAddress address2 = modelFactory.createAddress();
			IAddress address3 = modelFactory.createAddress();
			IAddress address4 = modelFactory.createAddress();

			address1.setCity("city1");
			address1.setStreet("street1");
			address1.setZipCode("zip1");

			address2.setCity("city2");
			address2.setStreet("street2");
			address2.setZipCode("zip2");

			address3.setCity("city3");
			address3.setStreet("street3");
			address3.setZipCode("zip3");

			address4.setCity("city4");
			address4.setStreet("street4");
			address4.setZipCode("zip4");

			IExpert ent7_1 = modelFactory.createExpert();
			IExpert ent7_2 = modelFactory.createExpert();

			((IPerson) ent7_1).setFirstName(TestData.N_ENT7_1);
			((IPerson) ent7_1).setLastName(TestData.N_ENT7_1);
			((IPerson) ent7_1).setAddress(address1);

			((IPerson) ent7_2).setFirstName(TestData.N_ENT7_2);
			((IPerson) ent7_2).setLastName(TestData.N_ENT7_2);
			((IPerson) ent7_2).setAddress(address2);

			IWorkPlatform ent1_1 = modelFactory.createPlatform();

			ITaskForce ent2_1 = modelFactory.createTaskForce();
			ent2_1.setExpert(ent7_1);
			ent2_1.setName(TestData.N_ENT2_1);
			ent2_1.setLastMeeting(new Date());
			ent2_1.setNextMeeting(new Date());
			ent2_1.setWorkPlatform(ent1_1);

			ITaskForce ent2_2 = modelFactory.createTaskForce();
			ent2_2.setExpert(ent7_2);
			ent2_2.setName(TestData.N_ENT2_2);
			ent2_2.setLastMeeting(new Date());
			ent2_2.setNextMeeting(new Date());
			ent2_2.setWorkPlatform(ent1_1);

			ent2_1.addComposedOf(ent2_2);
			ent2_2.addPartOf(ent2_1);

			ent7_1.addAdvisedTaskForce(ent2_1);
			ent7_2.addAdvisedTaskForce(ent2_2);

			em.persist(ent1_1);
			em.persist(ent7_1);
			em.persist(ent7_2);
			em.persist(ent2_1);
			em.persist(ent2_2);

			tx.commit();

			ent7_1Id = ((IPerson) ent7_1).getId();
			ent7_Id = ((IPerson) ent7_2).getId();

			ent2_1Id = ent2_1.getId();
			ent2_2Id = ent2_2.getId();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
