package dst.ass1.jpa.dao;

import java.util.Date;
import java.util.HashMap;

import dst.ass1.jpa.model.IExpert;

public interface IExpertDAO extends GenericDAO<IExpert>{

	public HashMap<IExpert, Date> findNextTaskForceMeetingOfExperts();

}
