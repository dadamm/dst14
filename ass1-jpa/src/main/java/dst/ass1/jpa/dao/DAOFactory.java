package dst.ass1.jpa.dao;

import dst.ass1.jpa.dao.impl.ExpertDao;
import dst.ass1.jpa.dao.impl.MembershipDao;
import dst.ass1.jpa.dao.impl.MetadataDao;
import dst.ass1.jpa.dao.impl.TaskDao;
import dst.ass1.jpa.dao.impl.TaskForceDao;
import dst.ass1.jpa.dao.impl.TaskProcessingDao;
import dst.ass1.jpa.dao.impl.TaskWorkerDao;
import dst.ass1.jpa.dao.impl.UserDao;
import dst.ass1.jpa.dao.impl.WorkPlatformDao;

public class DAOFactory {

	private org.hibernate.Session session;

	public DAOFactory(org.hibernate.Session session) {
		this.session = session;
	}

	public IWorkPlatformDAO getPlatformDAO() {
		return new WorkPlatformDao(session);
	}

	public IExpertDAO getExpertDAO() {
		return new ExpertDao(session);
	}

	public ITaskForceDAO getTaskForceDAO() {
		return new TaskForceDao(session);
	}

	public ITaskWorkerDAO getTaskWorkerDAO() {
		return new TaskWorkerDao(session);
	}

	public IMetadataDAO getMetadataDAO() {
		return new MetadataDao(session);
	}

	public ITaskProcessingDAO getTaskProcessingDAO() {
		return new TaskProcessingDao(session);
	}

	public ITaskDAO getTaskDAO() {
		return new TaskDao(session);
	}

	public IMembershipDAO getMembershipDAO() {
		return new MembershipDao(session);
	}

	public IUserDAO getUserDAO() {
		return new UserDao(session);
	}

}
