package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.util.Constants;

public class Test_2b01_2 extends AbstractTest {

	@Test
	public void testNamedQueryExists() {
		boolean exists = false;
		try {
			em.createNamedQuery(Constants.Q_TASKFORCESOFEXPERT);
			exists = true;
		} catch (Exception e) {
			exists = false;
		}

		assertTrue(exists);
	}

	@Test
	public void testFindWorkersInVienna() {

		// retrieve a new/fresh EntityManager, so that no entities can be cached
		EntityManager freshEM = getFreshEntityManager();

		DAOFactory daoFactory = new DAOFactory((Session) freshEM.getDelegate());
		HashMap<IExpert, Date> map = daoFactory.getExpertDAO()
				.findNextTaskForceMeetingOfExperts();
		assertEquals(0, map.size());

		freshEM.clear();
		freshEM.close();
	}
}
