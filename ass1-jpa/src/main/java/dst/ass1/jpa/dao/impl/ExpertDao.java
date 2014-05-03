package dst.ass1.jpa.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dst.ass1.jpa.dao.IExpertDAO;
import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.impl.Expert;

public class ExpertDao implements IExpertDAO {
	
	private final static Logger logger = LoggerFactory.getLogger(ExpertDao.class);
	
	private Session session;
	
	public ExpertDao(Session session) {
		this.session = session;
	}

	@Override
	public IExpert findById(Long id) {
		logger.trace("call findById method in ExpertDao with id {}", id);
		return (IExpert) session.createCriteria(Expert.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public List<IExpert> findAll() {
		logger.trace("call findAll method in ExpertDao");
		@SuppressWarnings("unchecked")
		List<IExpert> list = session.createCriteria(Expert.class).list();
		return list;
	}

	@Override
	public HashMap<IExpert, Date> findNextTaskForceMeetingOfExperts() {
		HashMap<IExpert, Date> expertMap = new HashMap<IExpert, Date>();
		@SuppressWarnings("unchecked")
		List<IExpert> experts = session.getNamedQuery("taskforcesOfExpert").list();
		for(IExpert expert : experts) {
			Date smallestDate = null;
			for(ITaskForce taskForce : expert.getAdvisedTaskForces()) {
				if (smallestDate == null || smallestDate.after(taskForce.getNextMeeting())) {
					smallestDate = taskForce.getNextMeeting();
				}
			}
			expertMap.put(expert, smallestDate);
		}
		return expertMap;
	}

}
