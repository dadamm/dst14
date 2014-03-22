package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import dst.ass1.jpa.dao.ITaskProcessingDAO;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.impl.TaskProcessing;

public class TaskProcessingDao implements ITaskProcessingDAO {
	
	private Session session;

	public TaskProcessingDao(Session session) {
		this.session = session;
	}

	@Override
	public ITaskProcessing findById(Long id) {
		Transaction transaction = session.beginTransaction();
		ITaskProcessing taskProcessing = (ITaskProcessing) session.createCriteria(TaskProcessing.class).add(Restrictions.eq("id", id)).uniqueResult();
		transaction.commit();
		return taskProcessing;
	}

	@Override
	public List<ITaskProcessing> findAll() {
		Transaction transaction = session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<ITaskProcessing> list = (List<ITaskProcessing>) session.createCriteria(TaskProcessing.class).list();
		transaction.commit();
		return list;
	}

}
