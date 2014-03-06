package dst.ass1.jpa.model;

import java.util.Date;
import java.util.List;

public interface ITaskWorker {
	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public Integer getWorkUnitCapacity();

	public void setWorkUnitCapacity(Integer workUnits);

	public String getLocation();

	public void setLocation(String location);

	public Date getJoinedDate();

	public void setJoinedDate(Date joined);

	public Date getLastTraining();

	public void setLastTraining(Date lastTraining);

	public ITaskForce getTaskForce();

	public void setTaskForce(ITaskForce taskForce);

	public List<ITaskProcessing> getTaskProcessings();

	public void setTaskProcessings(List<ITaskProcessing> processings);

	public void addTaskProcessing(ITaskProcessing processing);
}
