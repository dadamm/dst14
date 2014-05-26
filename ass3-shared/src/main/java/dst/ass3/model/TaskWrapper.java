package dst.ass3.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TaskWrapper implements ITaskWrapper {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "taskid")
	private Long taskId;
	
	@Enumerated(EnumType.STRING)
	private LifecycleState state;
	
	@Column(name = "ratedby", length = 100)
	private String ratedBy;
	
	@Enumerated(EnumType.STRING)
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
		return "TaskWrapper [id=" + id + ", taskId=" + taskId + ", status=" + state
				+ ", ratedBy=" + ratedBy + ", complexity=" + complexity + "]";
	}

}
