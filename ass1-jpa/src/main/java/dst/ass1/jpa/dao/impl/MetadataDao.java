package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import dst.ass1.jpa.dao.IMetadataDAO;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.impl.Metadata;

public class MetadataDao implements IMetadataDAO {
	
	private Session session;
	
	public MetadataDao(Session session) {
		this.session = session;
	}

	@Override
	public IMetadata findById(Long id) {
		Transaction transaction = session.beginTransaction();
		IMetadata membership = (IMetadata) session.createCriteria(Metadata.class).add(Restrictions.eq("id", id)).uniqueResult();
		transaction.commit();
		return membership;
	}

	@Override
	public List<IMetadata> findAll() {
		Transaction transaction = session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<IMetadata> list = (List<IMetadata>) session.createCriteria(Metadata.class).list();
		transaction.commit();
		return list;
	}

}
