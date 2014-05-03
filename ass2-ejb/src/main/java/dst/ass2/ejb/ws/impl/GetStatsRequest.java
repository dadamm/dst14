package dst.ass2.ejb.ws.impl;

import dst.ass2.ejb.ws.IGetStatsRequest;

public class GetStatsRequest implements IGetStatsRequest {
	
	private int maxProcessing;

	public GetStatsRequest() {
	}

	public GetStatsRequest(int maxProcessing) {
		this.maxProcessing = maxProcessing;
	}

	@Override
	public int getMaxProcessings() {
		return this.maxProcessing;
	}

	@Override
	public void setMaxProcessings(int maxProcessings) {
		this.maxProcessing = maxProcessings;

	}

}
