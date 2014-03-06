package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class TestEntity6 extends AbstractTest {

	private Long ent6_1Id;
	private Long ent6_2Id;
	private Long ent6_3Id;

	@Test
	public void testEntity() {
		Session session = (Session) em.getDelegate();
		DAOFactory daoFactory = new DAOFactory(session);

		List<IMetadata> list = daoFactory.getMetadataDAO()
				.findAll();

		assertNotNull(list);
		assertEquals(3, list.size());

		IMetadata ent6_1 = daoFactory.getMetadataDAO().findById(
				ent6_1Id);
		IMetadata ent6_2 = daoFactory.getMetadataDAO().findById(
				ent6_2Id);
		IMetadata ent6_3 = daoFactory.getMetadataDAO().findById(
				ent6_3Id);

		assertEquals(ent6_1Id, ent6_1.getId());
		assertEquals(TestData.N_ENT6_1, ent6_1.getContext());
		assertNotNull(ent6_1.getSettings());
		assertEquals(3, ent6_1.getSettings().size());
		assertEquals("param1", ent6_1.getSettings().get(0));
		assertEquals("param2", ent6_1.getSettings().get(1));
		assertEquals("param3", ent6_1.getSettings().get(2));

		assertEquals(ent6_2Id, ent6_2.getId());
		assertEquals(TestData.N_ENT6_2, ent6_2.getContext());
		assertNotNull(ent6_2.getSettings());
		assertEquals(1, ent6_2.getSettings().size());
		assertEquals("param4", ent6_2.getSettings().get(0));

		assertEquals(ent6_3Id, ent6_3.getId());
		assertEquals(TestData.N_ENT6_3, ent6_3.getContext());
		assertNotNull(ent6_3.getSettings());
		assertEquals(1, ent6_3.getSettings().size());
		assertEquals("param5", ent6_3.getSettings().get(0));

	}

	@Test
	public void testEntityJdbc() throws ClassNotFoundException,
			SQLException {
		String sql = "SELECT id, " +
				Constants.M_CONTEXT + " " +
				"FROM " + Constants.T_METADATA + " ORDER BY id ASC";
		Statement stmt = jdbcConnection.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {

			long id = rs.getLong("id");

			if (id == ent6_1Id.longValue()) {
				assertEquals((long) ent6_1Id, rs.getLong("id"));
				assertEquals(TestData.N_ENT6_1, rs.getString(Constants.M_CONTEXT));
			} else if (id == ent6_2Id.longValue()) {
				assertEquals((long) ent6_2Id, rs.getLong("id"));
				assertEquals(TestData.N_ENT6_2, rs.getString(Constants.M_CONTEXT));
			} else if (id == ent6_3Id.longValue()) {
				assertEquals((long) ent6_3Id, rs.getLong("id"));
				assertEquals(TestData.N_ENT6_3, rs.getString(Constants.M_CONTEXT));
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

			IMetadata ent6_1 = modelFactory.createMetadata();
			ent6_1.setContext(TestData.N_ENT6_1);
			ent6_1.addSetting("param1");
			ent6_1.addSetting("param2");
			ent6_1.addSetting("param3");

			IMetadata ent6_2 = modelFactory.createMetadata();
			ent6_2.setContext(TestData.N_ENT6_2);
			ent6_2.addSetting("param4");

			IMetadata ent6_3 = modelFactory.createMetadata();
			ent6_3.setContext(TestData.N_ENT6_3);
			ent6_3.addSetting("param5");

			em.persist(ent6_1);
			em.persist(ent6_2);
			em.persist(ent6_3);

			tx.commit();

			ent6_1Id = ent6_1.getId();
			ent6_2Id = ent6_2.getId();
			ent6_3Id = ent6_3.getId();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
