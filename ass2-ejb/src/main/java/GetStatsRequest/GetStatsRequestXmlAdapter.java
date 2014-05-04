package GetStatsRequest;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import dst.ass2.ejb.ws.IGetStatsRequest;
import dst.ass2.ejb.ws.impl.GetStatsRequest;

public class GetStatsRequestXmlAdapter extends XmlAdapter<String, IGetStatsRequest> {

	@Override
	public IGetStatsRequest unmarshal(String v) throws Exception {
		return new GetStatsRequest(Integer.parseInt(v));
	}

	@Override
	public String marshal(IGetStatsRequest v) throws Exception {
		return new Integer(v.getMaxProcessings()).toString();
	}

}
