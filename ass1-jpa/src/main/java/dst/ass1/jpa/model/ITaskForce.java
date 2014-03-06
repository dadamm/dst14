package dst.ass1.jpa.model;

import java.util.Date;
import java.util.List;

public interface ITaskForce {

	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public Date getLastMeeting();

	public void setLastMeeting(Date lastMeeting);

	public Date getNextMeeting();

	public void setNextMeeting(Date nextMeeting);

	public List<ITaskForce> getComposedOf();

	public void setComposedOf(List<ITaskForce> composedOf);

	public void addComposedOf(ITaskForce o);

	public List<ITaskForce> getPartOf();

	public void setPartOf(List<ITaskForce> partOf);

	public void addPartOf(ITaskForce o);

	public List<ITaskWorker> getTaskWorkers();

	public void setTaskWorkers(List<ITaskWorker> worker);

	public void addTaskWorker(ITaskWorker worker);

	public IExpert getExpert();

	public void setExpert(IExpert expert);

	public IWorkPlatform getWorkPlatform();

	public void setWorkPlatform(IWorkPlatform platform);

}
