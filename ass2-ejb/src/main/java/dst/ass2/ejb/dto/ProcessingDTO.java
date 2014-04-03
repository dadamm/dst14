package dst.ass2.ejb.dto;

import java.util.Date;

public class ProcessingDTO {

	private Date startDate;
	private Date endDate;
	private int numWorkUnits;

	public ProcessingDTO() {
	}

	public ProcessingDTO(Date startDate, Date endDate, int cpus) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.numWorkUnits = cpus;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getNumWorkUnits() {
		return numWorkUnits;
	}

	public void setNumWorkUnits(int numWorkUnits) {
		this.numWorkUnits = numWorkUnits;
	}

	@Override
	public String toString() {
		return startDate.toString() + " -- " + endDate.toString() + " ("
				+ numWorkUnits + ")";
	}

}
