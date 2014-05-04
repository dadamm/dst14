package dst.ass2.ejb.session;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.soap.Addressing;

import GetStatsRequest.GetStatsRequestXmlAdapter;
import GetStatsRequest.GetStatsResponseXmlAdapter;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass2.ejb.dto.StatisticsDTO;
import dst.ass2.ejb.session.exception.WebServiceException;
import dst.ass2.ejb.session.interfaces.ITaskStatisticsBean;
import dst.ass2.ejb.ws.Constants;
import dst.ass2.ejb.ws.IGetStatsRequest;
import dst.ass2.ejb.ws.IGetStatsResponse;
import dst.ass2.ejb.ws.impl.GetStatsResponse;

@Stateless
@Remote(ITaskStatisticsBean.class)
@Addressing
@WebService(name = Constants.NAME,
	serviceName = Constants.SERVICE_NAME,
	portName = Constants.PORT_NAME,
	targetNamespace = Constants.NAMESPACE,
	endpointInterface = "dst.ass2.ejb.session.interfaces.ITaskStatisticsBean")
public class TaskStatisticsBean implements ITaskStatisticsBean {
	
	@PersistenceContext private EntityManager entityManager;
	
	private boolean workPlatformExists(String name) {
		String sql = "select wp from WorkPlatform wp where name = :name";
		try {
			entityManager.createQuery(sql).setParameter("name", name).getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}
	
	private List<ITaskProcessing> getTaskProcessings(String workplatformName, int maxResults) {
		String sql = "select distinct tp from Task t "
				+ "join t.taskProcessing tp "
				+ "join tp.taskWorkers tw "
				+ "join tw.taskForce tf "
				+ "join tf.workPlatform wp "
				+ "where wp.name = :name and tp.status = 'FINISHED'";
		Query query = entityManager.createQuery(sql);
		query.setParameter("name", workplatformName);
		query.setMaxResults(maxResults);
		
		@SuppressWarnings("unchecked")
		List<ITaskProcessing> taskProcessings = query.getResultList();
		return taskProcessings;
	}

	@Override
	@Action(input  = "http://localhost:8080/" + Constants.SERVICE_NAME + "/inputAction",
     	output = "http://localhost:8080/" + Constants.SERVICE_NAME + "/outputAction",
     	fault = {@FaultAction(className = WebServiceException.class, value = "http://localhost:8080/" + Constants.SERVICE_NAME + "/failAction")})
	@WebMethod(operationName = Constants.OP_GET_STATS)
	public @XmlJavaTypeAdapter(GetStatsResponseXmlAdapter.class) IGetStatsResponse getStatisticsForPlatform(
			@XmlJavaTypeAdapter(GetStatsRequestXmlAdapter.class) @WebParam(partName = "request") IGetStatsRequest request,
			@WebParam(header = true, partName = "name") String name) throws WebServiceException {
		if(request == null || name == null || !workPlatformExists(name)) throw new WebServiceException("missing or illegal arguments");
		
		List<ITaskProcessing> taskProcessings = getTaskProcessings(name, request.getMaxProcessings());
		
		StatisticsDTO statisticsDTO = new StatisticsDTO();
		statisticsDTO.setName(name);
		for(ITaskProcessing taskProcessing : taskProcessings) {
			statisticsDTO.addProcessing(taskProcessing);
		}
		return new GetStatsResponse(statisticsDTO);
	}

}
