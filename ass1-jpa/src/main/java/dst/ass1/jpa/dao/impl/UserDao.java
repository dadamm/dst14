package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dst.ass1.jpa.dao.IUserDAO;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.impl.User;

public class UserDao implements IUserDAO {
	
	private final static Logger logger = LoggerFactory.getLogger(UserDao.class);
	
	private Session session;

	public UserDao(Session session) {
		this.session = session;
	}

	@Override
	public IUser findById(Long id) {
		logger.trace("call findById method in UserDao with id {}", id);
		return (IUser) session.createCriteria(User.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<IUser> findAll() {
		logger.trace("call findAll method in UserDao");
		@SuppressWarnings("unchecked")
		List<IUser> list = session.createCriteria(User.class).list();
		return list;
	}

}
