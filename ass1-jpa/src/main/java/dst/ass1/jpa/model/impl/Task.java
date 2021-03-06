package dst.ass1.jpa.model.impl;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.util.Constants;

//@NamedQuery(
//		name = "allFinishedTasks",
//		query = "select t from Task t join t.taskProcessing tp where tp.status =  dst.ass1.jpa.model.TaskStatus.FINISHED"
//)

@Entity
@Table(name = Constants.T_TASK)
public class Task implements ITask {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
//	@Column(name = "assignedworkunits")
	@Transient
	private Integer assignedWorkUnits;
	
//	@Column(name = "processingtime")
	@Transient
	private Integer processingTime;
	
	@Column(name = "ispaid")
	private boolean isPaid;
	
	@OneToOne(targetEntity = Metadata.class, optional = false)
	@JoinColumn(name = "metadata_id")
	private IMetadata metadata;
	
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private IUser user;
	
	@OneToOne(targetEntity = TaskProcessing.class, optional = false, cascade = CascadeType.ALL)
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
		if(assignedWorkUnits == null) {
			int sumWorkUnits = 0;
			for(ITaskWorker taskWorker : getTaskProcessing().getTaskWorkers()) {
				sumWorkUnits += taskWorker.getWorkUnitCapacity();
			}
			return sumWorkUnits;
		} else {
			return this.assignedWorkUnits;
		}
	}

	@Override
	public void setAssignedWorkUnits(Integer workUnits) {
		this.assignedWorkUnits = workUnits;
	}

	@Override
	public Integer getProcessingTime() {
		if(processingTime == null || processingTime == 0) {
			Long result = getTaskProcessing().getEnd().getTime() - getTaskProcessing().getStart().getTime();
			return result.intValue();
		} else {
			return this.processingTime;
		}
	}

	@Override
	public void setProcessingTime(Integer processingTime) {
		this.processingTime = processingTime;
	}

	@Override
	public boolean isPaid() {
		return this.isPaid;
	}

	@Override
	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
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
