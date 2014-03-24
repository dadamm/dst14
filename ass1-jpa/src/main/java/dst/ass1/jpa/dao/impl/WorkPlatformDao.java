package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import dst.ass1.jpa.dao.IWorkPlatformDAO;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.model.impl.WorkPlatform;

public class WorkPlatformDao implements IWorkPlatformDAO {
	
	private Session session;

	public WorkPlatformDao(Session session) {
		this.session = session;
	}

	@Override
	public IWorkPlatform findById(Long id) {
		return (IWorkPlatform) session.createCriteria(WorkPlatform.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<IWorkPlatform> findAll() {
		@SuppressWarnings("unchecked")
		List<IWorkPlatform> list = (List<IWorkPlatform>) session.createCriteria(WorkPlatform.class).list();
		return list;
	}

}
