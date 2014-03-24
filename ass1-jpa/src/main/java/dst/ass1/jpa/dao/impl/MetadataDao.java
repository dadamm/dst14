package dst.ass1.jpa.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dst.ass1.jpa.dao.IMetadataDAO;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.impl.Metadata;

public class MetadataDao implements IMetadataDAO {
	
	private final static Logger logger = LoggerFactory.getLogger(MetadataDao.class);
	
	private Session session;
	
	public MetadataDao(Session session) {
		this.session = session;
	}

	@Override
	public IMetadata findById(Long id) {
		logger.trace("call findById method in MetadataDao with id {}", id);
		return (IMetadata) session.createCriteria(Metadata.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<IMetadata> findAll() {
		logger.trace("call findAll method in MetadataDao");
		@SuppressWarnings("unchecked")
		List<IMetadata> list = (List<IMetadata>) session.createCriteria(Metadata.class).list();
		return list;
	}

}
