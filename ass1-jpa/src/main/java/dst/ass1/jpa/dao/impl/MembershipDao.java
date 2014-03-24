package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dst.ass1.jpa.dao.IMembershipDAO;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.impl.Membership;

public class MembershipDao implements IMembershipDAO {
	
	private final static Logger logger = LoggerFactory.getLogger(Membership.class);
	
	private Session session;
	
	public MembershipDao(Session session) {
		this.session = session;
	}

	@Override
	public IMembership findById(Long id) {
		logger.trace("call findById method in MembershipDao with id {}", id);
		return (IMembership) session.createCriteria(Membership.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<IMembership> findAll() {
		logger.trace("call findAll method in MembershipDao");
		@SuppressWarnings("unchecked")
		List<IMembership> list = (List<IMembership>) session.createCriteria(Membership.class).list();
		return list;
	}

}
