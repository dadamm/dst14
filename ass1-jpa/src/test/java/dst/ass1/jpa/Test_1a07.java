package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.JdbcHelper;

public class Test_1a07 extends AbstractTest {

	private Long ent5_1Id;
	private Long ent6_1Id;

	@Test
	public void testAssociation() {
		Session session = (Session) em.getDelegate();
		DAOFactory daoFactory = new DAOFactory(session);
		ITask ent5_1 = daoFactory.getTaskDAO().findById(ent5_1Id);
		assertNotNull(ent5_1);
		assertEquals(ent6_1Id, ent5_1.getMetadata().getId());
	}

	@Test
	public void testAssociationJdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(JdbcHelper.isColumnInTable(Constants.T_TASK,
				Constants.I_METADATA, jdbcConnection));
		assertFalse(JdbcHelper.isColumnInTable(Constants.T_METADATA, Constants.I_TASK,
				jdbcConnection));
		assertTrue(JdbcHelper.isIndex(Constants.T_TASK, Constants.I_METADATA, false,
				jdbcConnection));
	}

	public void setUpDatabase() {
		IMetadata ent6_1 = modelFactory.createMetadata();
		ITask ent5_1 = modelFactory.createTask();
		ent5_1.setMetadata(ent6_1);

		ITaskProcessing exec = modelFactory.createTaskProcessing();
		ent5_1.setTaskProcessing(exec);

		IUser u1 = modelFactory.createUser();
		u1.setUsername(dst.ass1.jpa.util.test.TestData.N_USER_1);
		u1.addTask(ent5_1);
		ent5_1.setUser(u1);

		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(u1);
			em.persist(ent6_1);
			em.persist(exec);
			em.persist(ent5_1);
			tx.commit();

			ent5_1Id = ent5_1.getId();
			ent6_1Id = ent6_1.getId();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}

	}

}
