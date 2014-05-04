package GetStatsRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import dst.ass2.ejb.dto.StatisticsDTO;
import dst.ass2.ejb.ws.IGetStatsResponse;
import dst.ass2.ejb.ws.impl.GetStatsResponse;

public class GetStatsResponseXmlAdapter extends	XmlAdapter<String, IGetStatsResponse> {

	@Override
	public IGetStatsResponse unmarshal(String v) throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(StatisticsDTO.class).createUnmarshaller();
		ByteArrayInputStream bias = new ByteArrayInputStream(v.getBytes());
		StatisticsDTO statisticsDTO = (StatisticsDTO) unmarshaller.unmarshal(bias);
		return new GetStatsResponse(statisticsDTO);
	}

	@Override
	public String marshal(IGetStatsResponse v) throws Exception {
		Marshaller marshaller = JAXBContext.newInstance(StatisticsDTO.class).createMarshaller();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		marshaller.marshal(v.getStatistics(), baos);
		return baos.toString();
	}

}
