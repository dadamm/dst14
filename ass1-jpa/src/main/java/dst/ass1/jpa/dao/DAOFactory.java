package dst.ass1.jpa.dao;

public class DAOFactory {

	private org.hibernate.Session session;

	public DAOFactory(org.hibernate.Session session) {
		this.session = session;
	}

	public IWorkPlatformDAO getPlatformDAO() {
		// TODO
		return null;
	}

	public IExpertDAO getExpertDAO() {
		// TODO
		return null;
	}

	public ITaskForceDAO getTaskForceDAO() {
		// TODO
		return null;
	}

	public ITaskWorkerDAO getTaskWorkerDAO() {
		// TODO
		return null;
	}

	public IMetadataDAO getMetadataDAO() {
		// TODO
		return null;
	}

	public ITaskProcessingDAO getTaskProcessingDAO() {
		// TODO
		return null;
	}

	public ITaskDAO getTaskDAO() {
		// TODO
		return null;
	}

	public IMembershipDAO getMembershipDAO() {
		// TODO
		return null;
	}

	public IUserDAO getUserDAO() {
		// TODO
		return null;
	}

}
