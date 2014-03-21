package dst.ass1.jpa.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.IUser;

@Entity
public class Task implements ITask {
	
	@Id
	private Long id;
	
	@Column(name = "assignedworkunits")
	private Integer assignedWorkUnits;
	
	@Column(name = "processingtime")
	private Integer processingTime;
	
	@Column(name = "paid")
	private boolean paid;
	
	@ManyToOne(targetEntity = Metadata.class)
	@JoinColumn(name = "metadata_id")
	private IMetadata metadata;
	
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private IUser user;
	
	@MapsId
	@OneToOne(targetEntity = TaskProcessing.class)
	@JoinColumn(name = "taskprocessing_id")
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
