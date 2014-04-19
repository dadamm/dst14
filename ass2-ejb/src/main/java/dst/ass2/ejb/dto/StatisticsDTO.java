package dst.ass2.ejb.dto;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.ITaskProcessing;

@XmlRootElement(name = "stats")
public class StatisticsDTO {

	private String name;
	private List<ProcessingDTO> processings;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ProcessingDTO> getProcessings() {
		return processings;
	}

	public void setProcessings(List<ProcessingDTO> processings) {
		this.processings = processings;
	}

	public void addProcessing(ITaskProcessing e) {
		if (processings == null)
			processings = new LinkedList<ProcessingDTO>();
		int cpus = 0;
		for (ITaskWorker c : e.getTaskWorkers()) {
			cpus += c.getWorkUnitCapacity();
		}
		ProcessingDTO edto = new ProcessingDTO(e.getStart(), e.getEnd(), cpus);
		processings.add(edto);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ");
		sb.append(name);
		sb.append("\n");
		for (ProcessingDTO e : processings) {
			sb.append(e.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

}
