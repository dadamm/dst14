package dst.ass1.jpa.model;

import java.util.Date;
import java.util.List;

public interface ITaskProcessing {

	public Long getId();

	public void setId(Long id);

	public Date getStart();

	public void setStart(Date start);

	public Date getEnd();

	public void setEnd(Date end);

	public TaskStatus getStatus();

	public void setStatus(TaskStatus status);

	public List<ITaskWorker> getTaskWorkers();

	public void setTaskWorkers(List<ITaskWorker> list);

	public void addWorker(ITaskWorker worker);

	public ITask getTask();

	public void setTask(ITask task);

}
