package dst.ass1.jpa.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dst.ass1.jpa.dao.ITaskDAO;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.impl.Task;

public class TaskDao implements ITaskDAO {
	
	private final static Logger logger = LoggerFactory.getLogger(TaskDao.class);
	
	private Session session;
	
	public TaskDao(Session session) {
		this.session = session;
	}

	@Override
	public ITask findById(Long id) {
		logger.trace("call findById method in TaskDao with id {}", id);
		return (ITask) session.createCriteria(Task.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<ITask> findAll() {
		logger.trace("call findAll method in TaskDao");
		@SuppressWarnings("unchecked")
		List<ITask> list = (List<ITask>) session.createCriteria(Task.class).list();
		return list;
	}

	@Override
	public List<ITask> findTasksForUserAndContext(String user, String c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ITask> findTasksForStatusFinishedStartandFinish(Date start, Date finish) {
		// TODO Auto-generated method stub
		return null;
	}

}
