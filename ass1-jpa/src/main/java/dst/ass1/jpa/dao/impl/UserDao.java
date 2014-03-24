package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
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
		return (IUser) session.createCriteria(User.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<IUser> findAll() {
		@SuppressWarnings("unchecked")
		List<IUser> list = (List<IUser>) session.createCriteria(User.class).list();
		return list;
	}

}
