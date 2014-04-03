package dst.ass2.ejb.session.interfaces;

import dst.ass2.ejb.session.exception.WebServiceException;
import dst.ass2.ejb.ws.IGetStatsRequest;
import dst.ass2.ejb.ws.IGetStatsResponse;

/**
 * This is the interface of the statistics Web Service.
 */
public interface ITaskStatisticsBean {

	/**
	 * Get statistics for a given platform.
	 * @param request The request object with parameters
	 * @param request Name of the platform
	 * @return statistics for the platform with the specified name.
	 */
	IGetStatsResponse getStatisticsForPlatform(
			IGetStatsRequest request, 
			String platformName) throws WebServiceException;

}
