package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dst.ass1.jpa.dao.IWorkPlatformDAO;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.model.impl.WorkPlatform;

public class WorkPlatformDao implements IWorkPlatformDAO {
	
	private final static Logger logger = LoggerFactory.getLogger(WorkPlatformDao.class);
	
	private Session session;

	public WorkPlatformDao(Session session) {
		this.session = session;
	}

	@Override
	public IWorkPlatform findById(Long id) {
		logger.trace("call findById method in WorkPlatformDao with id {}", id);
		return (IWorkPlatform) session.createCriteria(WorkPlatform.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<IWorkPlatform> findAll() {
		logger.trace("call findAll method in WorkPlatformDao");
		@SuppressWarnings("unchecked")
		List<IWorkPlatform> list = session.createCriteria(WorkPlatform.class).list();
		return list;
	}

}
