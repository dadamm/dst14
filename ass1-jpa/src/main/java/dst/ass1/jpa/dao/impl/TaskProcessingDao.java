package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
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
		return (ITaskProcessing) session.createCriteria(TaskProcessing.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<ITaskProcessing> findAll() {
		@SuppressWarnings("unchecked")
		List<ITaskProcessing> list = (List<ITaskProcessing>) session.createCriteria(TaskProcessing.class).list();
		return list;
	}

}
