package dst.ass1.jpa.model.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.util.Constants;

@Entity
@Table(name = Constants.T_TASKFORCE)
public class TaskForce implements ITaskForce {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", length = 50)
	private String name;
	
	@Column(name = "lastmeeting")
	private Date lastMeeting;
	
	@Column(name = "nextmeeting")
	private Date nextMeeting;
	
	@ManyToMany(targetEntity = TaskForce.class)
	@JoinTable(name = "taskforce_composer_partof",
		joinColumns = @JoinColumn(name = "composertaskforce_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "partoftaskforce_id", referencedColumnName = "id"))
	private List<ITaskForce> composedOf;
	
	@ManyToMany(mappedBy = "composedOf", targetEntity = TaskForce.class)
	private List<ITaskForce> partOf;
	
	@OneToMany(mappedBy = "taskForce", targetEntity = TaskWorker.class)
	private List<ITaskWorker> taskWorkers;
	
	@ManyToOne(targetEntity = Expert.class)
	@JoinColumn(name = "expert_id")
	private IExpert expert;
	
	@ManyToOne(targetEntity = WorkPlatform.class)
	@JoinColumn(name = "workplatform_id")
	private IWorkPlatform workPlatform;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Date getLastMeeting() {
		return this.lastMeeting;
	}

	@Override
	public void setLastMeeting(Date lastMeeting) {
		this.lastMeeting = lastMeeting;
	}

	@Override
	public Date getNextMeeting() {
		return this.nextMeeting;
	}

	@Override
	public void setNextMeeting(Date nextMeeting) {
		this.nextMeeting = nextMeeting;
	}

	@Override
	public List<ITaskForce> getComposedOf() {
		return this.composedOf;
	}

	@Override
	public void setComposedOf(List<ITaskForce> composedOf) {
		this.composedOf = composedOf;
	}

	@Override
	public void addComposedOf(ITaskForce o) {
		this.composedOf.add(o);
	}

	@Override
	public List<ITaskForce> getPartOf() {
		return this.partOf;
	}

	@Override
	public void setPartOf(List<ITaskForce> partOf) {
		this.partOf = partOf;
	}

	@Override
	public void addPartOf(ITaskForce o) {
		this.partOf.add(o);
	}

	@Override
	public List<ITaskWorker> getTaskWorkers() {
		return this.taskWorkers;
	}

	@Override
	public void setTaskWorkers(List<ITaskWorker> worker) {
		this.taskWorkers = worker;
	}

	@Override
	public void addTaskWorker(ITaskWorker worker) {
		this.taskWorkers.add(worker);
	}

	@Override
	public IExpert getExpert() {
		return this.expert;
	}

	@Override
	public void setExpert(IExpert expert) {
		this.expert = expert;
	}

	@Override
	public IWorkPlatform getWorkPlatform() {
		return this.workPlatform;
	}

	@Override
	public void setWorkPlatform(IWorkPlatform platform) {
		this.workPlatform = platform;
	}

}
