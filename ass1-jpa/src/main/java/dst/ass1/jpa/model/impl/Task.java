package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.IUser;

public class Task implements ITask {
	
	private Long id;
	private Integer assignedWorkUnits;
	private Integer processingTime;
	private boolean paid;
	private IMetadata metadata;
	private IUser user;
	private ITaskProcessing taskProcessing;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Integer getAssignedWorkUnits() {
		return this.assignedWorkUnits;
	}

	@Override
	public void setAssignedWorkUnits(Integer workUnits) {
		this.assignedWorkUnits = workUnits;
	}

	@Override
	public Integer getProcessingTime() {
		return this.processingTime;
	}

	@Override
	public void setProcessingTime(Integer processingTime) {
		this.processingTime = processingTime;
	}

	@Override
	public boolean isPaid() {
		return this.paid;
	}

	@Override
	public void setPaid(boolean isPaid) {
		this.paid = isPaid;
	}

	@Override
	public IMetadata getMetadata() {
		return this.metadata;
	}

	@Override
	public void setMetadata(IMetadata metadata) {
		this.metadata = metadata;
	}

	@Override
	public IUser getUser() {
		return this.user;
	}

	@Override
	public void setUser(IUser user) {
		this.user = user;
	}

	@Override
	public ITaskProcessing getTaskProcessing() {
		return this.taskProcessing;
	}

	@Override
	public void setTaskProcessing(ITaskProcessing processing) {
		this.taskProcessing = processing;
	}

}
