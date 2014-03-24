package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import dst.ass1.jpa.dao.IUserDAO;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.impl.User;

public class UserDao implements IUserDAO {
	
	private Session session;

	public UserDao(Session session) {
		this.session = session;
	}

	@Override
	public IUser findById(Long id) {
		Transaction transaction = session.beginTransaction();
		IUser user = (IUser) session.createCriteria(User.class).add(Restrictions.eq("id", id)).uniqueResult();
		transaction.commit();
		return user;
	}

	@Override
	public List<IUser> findAll() {
		Transaction transaction = session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<IUser> list = (List<IUser>) session.createCriteria(User.class).list();
		transaction.commit();
		return list;
	}

}
