package dst.ass3.dto;

import java.io.Serializable;

public class InfoTaskWrapperDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long taskWrapperId;

	public InfoTaskWrapperDTO() {
	}

	public InfoTaskWrapperDTO(Long taskWrapperId) {
		super();
		this.taskWrapperId = taskWrapperId;
	}

	public Long getTaskWrapperId() {
		return taskWrapperId;
	}

	public void setTaskWrapperId(Long taskWrapperId) {
		this.taskWrapperId = taskWrapperId;
	}

}
