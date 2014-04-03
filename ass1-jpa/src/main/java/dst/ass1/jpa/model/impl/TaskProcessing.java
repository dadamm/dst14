package dst.ass1.jpa.model.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.TaskStatus;
import dst.ass1.jpa.util.Constants;

@Entity
@Table(name = Constants.T_TASKPROCESSING)
public class TaskProcessing implements ITaskProcessing {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "start")
	private Date start;
	
	@Column(name = "end")
	private Date end;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 10)
	private TaskStatus status;
	
	@ManyToMany(targetEntity = TaskWorker.class)
	@JoinTable(name = Constants.J_PROCESSING_TASKWORKER,
			joinColumns = @JoinColumn(name = "TaskProcessings_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "TaskWorkers_id", referencedColumnName = "id"))
	private List<ITaskWorker> taskWorkers;
	
	@OneToOne(targetEntity = Task.class)
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
		return this.status;
	}

	@Override
	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	@Override
	public List<ITaskWorker> getTaskWorkers() {
		if (taskWorkers == null) {
			return this.taskWorkers = new LinkedList<ITaskWorker>();
		} else {
			return this.taskWorkers;
		}
	}

	@Override
	public void setTaskWorkers(List<ITaskWorker> list) {
		this.taskWorkers = list;
	}

	@Override
	public void addWorker(ITaskWorker worker) {
		getTaskWorkers().add(worker);
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
