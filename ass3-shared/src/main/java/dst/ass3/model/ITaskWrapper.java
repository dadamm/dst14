package dst.ass3.model;

public interface ITaskWrapper {

	public Long getId();

	public void setId(Long id);

	public Long getTaskId();

	public void setTaskId(Long taskId);

	public LifecycleState getState();

	public void setState(LifecycleState status);

	public String getRatedBy();

	public void setRatedBy(String ratedBy);

	public Complexity getComplexity();

	public void setComplexity(Complexity complexity);
}
