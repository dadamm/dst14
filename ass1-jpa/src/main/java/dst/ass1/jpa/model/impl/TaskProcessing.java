package dst.ass1.jpa.model.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.TaskStatus;

@Entity
@Table(name = "task_processing")
public class TaskProcessing implements ITaskProcessing {
	
	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "start")
	private Date start;
	
	@Column(name = "end")
	private Date end;
	
	@Enumerated(EnumType.STRING)
	private TaskStatus taskStatus;
	
	@ManyToMany(targetEntity = TaskWorker.class)
	@JoinTable(name = "task_worker_processing_rel",
			joinColumns = @JoinColumn(name = "taskprocessing_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "taskworker_id", referencedColumnName = "id"))
	private List<ITaskWorker> taskWorkers;
	
	@OneToOne(targetEntity = Task.class)
	@PrimaryKeyJoinColumn
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
