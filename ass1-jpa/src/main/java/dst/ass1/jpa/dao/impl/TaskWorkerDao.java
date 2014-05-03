package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dst.ass1.jpa.dao.ITaskWorkerDAO;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.impl.TaskWorker;

public class TaskWorkerDao implements ITaskWorkerDAO {
	
	private final static Logger logger = LoggerFactory.getLogger(TaskWorkerDao.class);
	
	private Session session;

	public TaskWorkerDao(Session session) {
		this.session = session;
	}

	@Override
	public ITaskWorker findById(Long id) {
		logger.trace("call findById method in TaskWorkerDao with id {}", id);
		return (ITaskWorker) session.createCriteria(TaskWorker.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<ITaskWorker> findAll() {
		logger.trace("call findAll method in TaskWorkerDao");
		@SuppressWarnings("unchecked")
		List<ITaskWorker> list = session.createCriteria(TaskWorker.class).list();
		return list;
	}

}
