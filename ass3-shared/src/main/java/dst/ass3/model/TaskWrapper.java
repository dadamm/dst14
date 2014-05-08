package dst.ass3.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TaskWrapper implements ITaskWrapper {

	private Long id;
	private Long taskId;
	private LifecycleState state;
	private String ratedBy;
	private Complexity complexity;

	public TaskWrapper() {
	}

	public TaskWrapper(Long taskId, LifecycleState state, String ratedBy,
			Complexity complexity) {
		super();
		this.taskId = taskId;
		this.state = state;
		this.ratedBy = ratedBy;
		this.complexity = complexity;
	}

	@Id
	@GeneratedValue
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
		return "TaskWrapper [id=" + id + ", taskId=" + taskId + ", status=" + state
				+ ", ratedBy=" + ratedBy + ", complexity=" + complexity + "]";
	}

}
