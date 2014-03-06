package dst.ass1.jpa.model;

public interface ITask {

	public Long getId();

	public void setId(Long id);

	public Integer getAssignedWorkUnits();

	public void setAssignedWorkUnits(Integer workUnits);

	public Integer getProcessingTime();

	public void setProcessingTime(Integer processingTime);

	public boolean isPaid();

	public void setPaid(boolean isPaid);

	public IMetadata getMetadata();

	public void setMetadata(IMetadata metadata);

	public IUser getUser();

	public void setUser(IUser user);

	public ITaskProcessing getTaskProcessing();

	public void setTaskProcessing(ITaskProcessing processing);
}
