package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
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
		return (IMetadata) session.createCriteria(Metadata.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<IMetadata> findAll() {
		@SuppressWarnings("unchecked")
		List<IMetadata> list = (List<IMetadata>) session.createCriteria(Metadata.class).list();
		return list;
	}

}
