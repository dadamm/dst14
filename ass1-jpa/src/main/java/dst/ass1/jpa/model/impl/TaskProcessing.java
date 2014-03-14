package dst.ass1.jpa.model.impl;

import java.util.Date;
import java.util.List;

import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.TaskStatus;

public class TaskProcessing implements ITaskProcessing {
	
	private Long id;
	private Date start;
	private Date end;
	private TaskStatus taskStatus;
	private List<ITaskWorker> taskWorkers;
	private ITask task;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Date getStart() {
		return this.start;
	}

	@Override
	public void setStart(Date start) {
		this.start = start;
	}

	@Override
	public Date getEnd() {
		return this.end;
	}

	@Override
	public void setEnd(Date end) {
		this.end = end;
	}

	@Override
	public TaskStatus getStatus() {
		return this.taskStatus;
	}

	@Override
	public void setStatus(TaskStatus status) {
		this.taskStatus = status;
	}

	@Override
	public List<ITaskWorker> getTaskWorkers() {
		return this.taskWorkers;
	}

	@Override
	public void setTaskWorkers(List<ITaskWorker> list) {
		this.taskWorkers = list;
	}

	@Override
	public void addWorker(ITaskWorker worker) {
		this.taskWorkers.add(worker);
	}

	@Override
	public ITask getTask() {
		return this.task;
	}

	@Override
	public void setTask(ITask task) {
		this.task = task;
	}

}
