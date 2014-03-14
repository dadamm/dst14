package dst.ass1.jpa.model.impl;

import java.util.Date;
import java.util.List;

import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IWorkPlatform;

public class TaskForce implements ITaskForce {
	
	private Long id;
	private String name;
	private Date lastMeeting;
	private Date nextMeeting;
	private List<ITaskForce> composedOf;
	private List<ITaskForce> partOf;
	private List<ITaskWorker> taskWorkers;
	private IExpert expert;
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
