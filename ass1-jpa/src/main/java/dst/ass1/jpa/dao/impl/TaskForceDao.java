package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
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
		return (ITaskForce) session.createCriteria(TaskForce.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<ITaskForce> findAll() {
		@SuppressWarnings("unchecked")
		List<ITaskForce> list = (List<ITaskForce>) session.createCriteria(TaskForce.class).list();
		return list;
	}

}
