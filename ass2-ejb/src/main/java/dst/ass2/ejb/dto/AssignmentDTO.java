package dst.ass2.ejb.dto;

import java.io.Serializable;
import java.util.List;

public class AssignmentDTO implements Serializable {

	private static final long serialVersionUID = -6468125387570684307L;
	private Long platformId;
	private Integer numWorkUnits;
	private String context;
	private List<String> settings;
	private List<Long> workerIds;

	public AssignmentDTO(Long platformId, Integer numWorkUnits, String context,
			List<String> settings, List<Long> computerIds) {
		super();
		this.platformId = platformId;
		this.numWorkUnits = numWorkUnits;
		this.context = context;
		this.settings = settings;
		this.workerIds = computerIds;
	}

	public Long getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}
	public Integer getNumWorkUnits() {
		return numWorkUnits;
	}
	public void setNumWorkUnits(Integer numWorkUnits) {
		this.numWorkUnits = numWorkUnits;
	}
	public List<String> getSettings() {
		return settings;
	}
	public void setSettings(List<String> settings) {
		this.settings = settings;
	}
	public List<Long> getWorkerIds() {
		return workerIds;
	}
	public void setWorkerIds(List<Long> workerIds) {
		this.workerIds = workerIds;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((workerIds == null) ? 0 : workerIds.hashCode());
		result = prime * result + ((platformId == null) ? 0 : platformId.hashCode());
		result = prime * result + ((numWorkUnits == null) ? 0 : numWorkUnits.hashCode());
		result = prime * result + ((settings == null) ? 0 : settings.hashCode());
		result = prime * result
				+ ((context == null) ? 0 : context.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssignmentDTO other = (AssignmentDTO) obj;
		if (workerIds == null) {
			if (other.workerIds != null)
				return false;
		} else if (!workerIds.equals(other.workerIds))
			return false;
		if (platformId == null) {
			if (other.platformId != null)
				return false;
		} else if (!platformId.equals(other.platformId))
			return false;
		if (numWorkUnits == null) {
			if (other.numWorkUnits != null)
				return false;
		} else if (!numWorkUnits.equals(other.numWorkUnits))
			return false;
		if (settings == null) {
			if (other.settings != null)
				return false;
		} else if (!settings.equals(other.settings))
			return false;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AssignmentDTO [platformId=" + platformId + ", numWorkUnits=" + numWorkUnits
				+ ", context=" + context + ", settings=" + settings
				+ ", workerIds=" + workerIds + "]";
	}

}
