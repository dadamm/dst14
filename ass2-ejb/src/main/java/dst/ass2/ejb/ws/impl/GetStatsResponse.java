package dst.ass2.ejb.ws.impl;

import dst.ass2.ejb.dto.StatisticsDTO;
import dst.ass2.ejb.ws.IGetStatsResponse;

public class GetStatsResponse implements IGetStatsResponse {
	
	private StatisticsDTO statisticsDTO;

	public GetStatsResponse(StatisticsDTO statisticsDTO) {
		this.statisticsDTO = statisticsDTO;
	}

	@Override
	public StatisticsDTO getStatistics() {
		return this.statisticsDTO;
	}

}
