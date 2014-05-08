package dst.ass3.dto;

import java.io.Serializable;

import dst.ass3.model.ITaskWrapper;
import dst.ass3.model.Complexity;
import dst.ass3.model.LifecycleState;

public class ProcessTaskWrapperDTO implements Serializable {

	private static final long serialVersionUID = 2537272216199752190L;
	private Long id;
	private Long taskId;
	private LifecycleState state;
	private String ratedBy;
	private Complexity complexity;

	public ProcessTaskWrapperDTO() {
	}

	public ProcessTaskWrapperDTO(Long id, Long taskId, LifecycleState state,
			String ratedBy, Complexity complexity) {
		super();
		this.id = id;
		this.taskId = taskId;
		this.state = state;
		this.ratedBy = ratedBy;
		this.complexity = complexity;
	}

	public ProcessTaskWrapperDTO(ITaskWrapper task) {
		super();
		this.id = task.getId();
		this.taskId = task.getTaskId();
		this.state = task.getState();
		this.ratedBy = task.getRatedBy();
		this.complexity = task.getComplexity();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public LifecycleState getState() {
		return state;
	}

	public void setState(LifecycleState state) {
		this.state = state;
	}

	public String getRatedBy() {
		return ratedBy;
	}

	public void setRatedBy(String ratedBy) {
		this.ratedBy = ratedBy;
	}

	public Complexity getComplexity() {
		return complexity;
	}

	public void setComplexity(Complexity complexity) {
		this.complexity = complexity;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", taskId=" + taskId + ", ratedBy=" + ratedBy
				+ ", state=" + state + ", complexity=" + complexity + "]";
	}

}
