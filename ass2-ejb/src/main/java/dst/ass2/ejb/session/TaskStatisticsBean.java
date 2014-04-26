package dst.ass2.ejb.session;

import dst.ass2.ejb.session.exception.WebServiceException;
import dst.ass2.ejb.session.interfaces.ITaskStatisticsBean;
import dst.ass2.ejb.ws.IGetStatsRequest;
import dst.ass2.ejb.ws.IGetStatsResponse;

public class TaskStatisticsBean implements ITaskStatisticsBean {

	@Override
	public IGetStatsResponse getStatisticsForPlatform(
			IGetStatsRequest request, 
			String name) throws WebServiceException {
		// TODO
		return null;
	}

}
