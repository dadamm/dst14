package dst.ass1;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;

import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.IPerson;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.ModelFactory;
import dst.ass1.jpa.util.JdbcConnection;
import dst.ass1.jpa.util.JdbcHelper;

public abstract class AbstractTest {

	protected EntityManagerFactory emf;
	protected EntityManager em;
	protected JdbcConnection jdbcConnection;
	protected ModelFactory modelFactory;

	@Before
	public void init() throws Exception {
		emf = AbstractTestSuite.getEmf();
		em = emf.createEntityManager();
		jdbcConnection = new JdbcConnection();
		modelFactory = new ModelFactory();

		setUpDatabase();
	}

	@After
	public void clean() throws Exception {
		if (em.getTransaction().isActive())
			em.getTransaction().rollback();
		em.close();
		JdbcHelper.cleanTables(jdbcConnection);
		jdbcConnection.disconnect();
	}
	
	protected EntityManager getFreshEntityManager() {
		return emf.createEntityManager();
	}

	protected void setUpDatabase() throws Exception {
	}

	protected List<Long> getTaskIds(List<ITask> tasks) {
		List<Long> ids = new ArrayList<Long>();

		for (ITask task : tasks)
			ids.add(task.getId());

		return ids;
	}

	protected List<Long> getUserIds(List<IUser> users) {
		List<Long> ids = new ArrayList<Long>();

		for (IUser user : users)
			ids.add(((IPerson) user).getId());

		return ids;
	}
	
	protected List<Long> getWorkerIds(List<ITaskWorker> list) {
		List<Long> ids = new ArrayList<Long>();

		for (ITaskWorker o : list)
			ids.add(o.getId());

		return ids;
	}
	
	protected boolean checkMembership(Long userId, Long parentId,
			Double discount, List<IMembership> memberships) {
		
		for (IMembership membership : memberships) {
			IMembershipKey memId = membership.getId();
			assertNotNull(memId);

			IWorkPlatform mem = memId.getWorkPlatform();
			assertNotNull(mem);
			Long memIdL = mem.getId();
			assertNotNull(memIdL);

			IUser memUser = memId.getUser();
			assertNotNull(memUser);
			Long memUserId = ((IPerson) memUser).getId();
			assertNotNull(memUserId);

			Double memDiscount = membership.getDiscount();
			assertNotNull(memDiscount);

			assertNotNull(membership.getRegistration());

			if (memIdL.equals(parentId) && memUserId.equals(userId)
					&& memDiscount.equals(discount))
				return true;
		}

		return false;
	}

}
