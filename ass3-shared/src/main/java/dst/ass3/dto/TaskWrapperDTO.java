package dst.ass3.dto;

import java.io.Serializable;

import dst.ass3.model.ITaskWrapper;
import dst.ass3.model.Complexity;
import dst.ass3.model.LifecycleState;

public class TaskWrapperDTO implements Serializable, ITaskWrapper {

	private static final long serialVersionUID = 4134104076758220138L;
	private Long id;
	private Long taskId;
	private LifecycleState state;
	private String ratedBy;
	private Complexity complexity;

	public TaskWrapperDTO() {
	}

	public TaskWrapperDTO(Long id, Long taskId, LifecycleState state, String ratedBy,
			Complexity complexity) {
		super();
		this.id = id;
		this.taskId = taskId;
		this.state = state;
		this.ratedBy = ratedBy;
		this.complexity = complexity;
	}

	public TaskWrapperDTO(ITaskWrapper task) {
		super();
		this.id = task.getId();
		this.taskId = task.getTaskId();
		this.state = task.getState();
		this.ratedBy = task.getRatedBy();
		this.complexity = task.getComplexity();
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getTaskId() {
		return taskId;
	}

	@Override
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	@Override
	public LifecycleState getState() {
		return state;
	}

	@Override
	public void setState(LifecycleState state) {
		this.state = state;
	}

	@Override
	public String getRatedBy() {
		return ratedBy;
	}

	@Override
	public void setRatedBy(String ratedBy) {
		this.ratedBy = ratedBy;
	}

	@Override
	public Complexity getComplexity() {
		return complexity;
	}

	@Override
	public void setComplexity(Complexity complexity) {
		this.complexity = complexity;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", taskId=" + taskId + ", ratedBy=" + ratedBy
				+ ", complexity=" + complexity + ", state=" + state + "]";
	}

}
