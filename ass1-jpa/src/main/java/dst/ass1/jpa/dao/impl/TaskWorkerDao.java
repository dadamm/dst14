package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import dst.ass1.jpa.dao.ITaskWorkerDAO;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.impl.TaskWorker;

public class TaskWorkerDao implements ITaskWorkerDAO {
	
	private Session session;

	public TaskWorkerDao(Session session) {
		this.session = session;
	}

	@Override
	public ITaskWorker findById(Long id) {
		return (ITaskWorker) session.createCriteria(TaskWorker.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<ITaskWorker> findAll() {
		@SuppressWarnings("unchecked")
		List<ITaskWorker> list = (List<ITaskWorker>) session.createCriteria(TaskWorker.class).list();
		return list;
	}

}
