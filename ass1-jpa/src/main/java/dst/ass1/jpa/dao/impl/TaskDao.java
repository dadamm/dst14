package dst.ass1.jpa.dao.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dst.ass1.jpa.dao.ITaskDAO;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.TaskStatus;
import dst.ass1.jpa.model.impl.Task;
import dst.ass1.jpa.model.impl.TaskProcessing;

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
		List<ITask> list = session.createCriteria(Task.class).list();
		return list;
	}

	@Override
	public List<ITask> findTasksForUserAndContext(String user, String c) {
		logger.trace("call findTasksForUserAndContext for user {} and context {}", new Object[]{user, c});
		Criteria criteria = session.createCriteria(Task.class);
		if (user != null) {
			criteria.createCriteria("user").add(Restrictions.eq("username", user));
		}
		if (c != null) {
			criteria.createCriteria("metadata").add(Restrictions.eq("context", c));
		}
		@SuppressWarnings("unchecked")
		List<ITask> list = criteria.list();
		return list;
	}

	@Override
	public List<ITask> findTasksForStatusFinishedStartandFinish(Date start, Date finish) {
		List<ITask> tasks = new LinkedList<ITask>();
		
		TaskProcessing taskProcessing = new TaskProcessing();
		taskProcessing.setStatus(TaskStatus.FINISHED);
		taskProcessing.setStart(start);
		taskProcessing.setEnd(finish);
		
		Criteria criteria = session.createCriteria(TaskProcessing.class);
		criteria.add(Example.create(taskProcessing));
		@SuppressWarnings("unchecked")
		List<ITaskProcessing> tasksProcessings = criteria.list();
		for(ITaskProcessing p : tasksProcessings) {
			tasks.add(p.getTask());
		}
		return tasks;
	}

}
