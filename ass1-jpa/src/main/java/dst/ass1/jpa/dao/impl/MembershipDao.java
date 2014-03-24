package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import dst.ass1.jpa.dao.IMembershipDAO;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.impl.Membership;

public class MembershipDao implements IMembershipDAO {
	
	private Session session;
	
	public MembershipDao(Session session) {
		this.session = session;
	}

	@Override
	public IMembership findById(Long id) {
		return (IMembership) session.createCriteria(Membership.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<IMembership> findAll() {
		@SuppressWarnings("unchecked")
		List<IMembership> list = (List<IMembership>) session.createCriteria(Membership.class).list();
		return list;
	}

}
