package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dst.ass1.jpa.dao.ITaskForceDAO;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.impl.TaskForce;

public class TaskForceDao implements ITaskForceDAO {
	
	private final static Logger logger = LoggerFactory.getLogger(TaskForceDao.class);
	
	private Session session;

	public TaskForceDao(Session session) {
		this.session = session;
	}

	@Override
	public ITaskForce findById(Long id) {
		logger.trace("call findById method in TaskForceDao with id {}", id);
		return (ITaskForce) session.createCriteria(TaskForce.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<ITaskForce> findAll() {
		logger.trace("call findAll method in TaskForceDao");
		@SuppressWarnings("unchecked")
		List<ITaskForce> list = session.createCriteria(TaskForce.class).list();
		return list;
	}

}
