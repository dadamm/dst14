package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
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
		Transaction transaction = session.beginTransaction();
		ITaskWorker taskWorker = (ITaskWorker) session.createCriteria(TaskWorker.class).add(Restrictions.eq("id", id)).uniqueResult();
		transaction.commit();
		return taskWorker;
	}

	@Override
	public List<ITaskWorker> findAll() {
		Transaction transaction = session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<ITaskWorker> list = (List<ITaskWorker>) session.createCriteria(TaskWorker.class).list();
		transaction.commit();
		return list;
	}

}
