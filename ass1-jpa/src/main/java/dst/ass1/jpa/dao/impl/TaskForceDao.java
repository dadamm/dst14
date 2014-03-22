package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import dst.ass1.jpa.dao.ITaskForceDAO;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.impl.TaskForce;

public class TaskForceDao implements ITaskForceDAO {
	
	private Session session;

	public TaskForceDao(Session session) {
		this.session = session;
	}

	@Override
	public ITaskForce findById(Long id) {
		Transaction transaction = session.beginTransaction();
		ITaskForce taskForce = (ITaskForce) session.createCriteria(TaskForce.class).add(Restrictions.eq("id", id)).uniqueResult();
		transaction.commit();
		return taskForce;
	}

	@Override
	public List<ITaskForce> findAll() {
		Transaction transaction = session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<ITaskForce> list = (List<ITaskForce>) session.createCriteria(TaskForce.class).list();
		transaction.commit();
		return list;
	}

}
