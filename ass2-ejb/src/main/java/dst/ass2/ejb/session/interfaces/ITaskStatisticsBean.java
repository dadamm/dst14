package dst.ass2.ejb.session.interfaces;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;

import GetStatsRequest.GetStatsRequestXmlAdapter;
import GetStatsRequest.GetStatsResponseXmlAdapter;
import dst.ass2.ejb.session.exception.WebServiceException;
import dst.ass2.ejb.ws.Constants;
import dst.ass2.ejb.ws.IGetStatsRequest;
import dst.ass2.ejb.ws.IGetStatsResponse;

/**
 * This is the interface of the statistics Web Service.
 */
@WebService(name = Constants.NAME,
	serviceName = Constants.SERVICE_NAME,
	portName = Constants.PORT_NAME,
	targetNamespace = Constants.NAMESPACE)
public interface ITaskStatisticsBean {

	/**
	 * Get statistics for a given platform.
	 * @param request The request object with parameters
	 * @param request Name of the platform
	 * @return statistics for the platform with the specified name.
	 */
	@Action(input  = "http://localhost:8080/" + Constants.SERVICE_NAME + "/inputAction",
 		output = "http://localhost:8080/" + Constants.SERVICE_NAME + "/outputAction",
 		fault = {@FaultAction(className = WebServiceException.class, value = "http://localhost:8080/" + Constants.SERVICE_NAME + "/failAction")})
	@WebMethod(operationName = Constants.OP_GET_STATS)
	@XmlJavaTypeAdapter(GetStatsResponseXmlAdapter.class) IGetStatsResponse getStatisticsForPlatform(
			@XmlJavaTypeAdapter(GetStatsRequestXmlAdapter.class) @WebParam(partName = "request") IGetStatsRequest request, 
			@WebParam(header = true, partName = "name")String platformName) throws WebServiceException;

}
