package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
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
		Transaction transaction = session.beginTransaction();
		IWorkPlatform workPlatform = (IWorkPlatform) session.createCriteria(WorkPlatform.class).add(Restrictions.eq("id", id)).uniqueResult();
		transaction.commit();
		return workPlatform;
	}

	@Override
	public List<IWorkPlatform> findAll() {
		Transaction transaction = session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<IWorkPlatform> list = (List<IWorkPlatform>) session.createCriteria(WorkPlatform.class).list();
		transaction.commit();
		return list;
	}

}
