package dst.ass3.dto;

import java.io.Serializable;

public class NewTaskWrapperDTO implements Serializable {

	private static final long serialVersionUID = 843972285375484461L;
	private Long taskId;

	public NewTaskWrapperDTO() {
	}

	public NewTaskWrapperDTO(Long taskId) {
		this.taskId = taskId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

}
