package dst.ass1.jpa.model.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.validator.WorkUnitCapacity;

public class TaskWorker implements ITaskWorker {
	
	private Long id;
	
	@Size(min = 5, max = 25)
	private String name;
	
	@WorkUnitCapacity(min = 4, max = 8)
	private Integer workUnitCapacity;
	
	@Pattern(regexp = "[A-Z]{3}-[A-Z]{3}@[0-9]{4}")
	private String location;
	
	@Past
	private Date joinedDate;
	
	@Past
	private Date lastTraining;
	
	private ITaskForce taskForce;
	private List<ITaskProcessing> taskProcessings;

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
	public Integer getWorkUnitCapacity() {
		return this.workUnitCapacity;
	}

	@Override
	public void setWorkUnitCapacity(Integer workUnits) {
		this.workUnitCapacity = workUnits;
	}

	@Override
	public String getLocation() {
		return this.location;
	}

	@Override
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public Date getJoinedDate() {
		return this.joinedDate;
	}

	@Override
	public void setJoinedDate(Date joined) {
		this.joinedDate = joined;
	}

	@Override
	public Date getLastTraining() {
		return this.lastTraining;
	}

	@Override
	public void setLastTraining(Date lastTraining) {
		this.lastTraining = lastTraining;
	}

	@Override
	public ITaskForce getTaskForce() {
		return this.taskForce;
	}

	@Override
	public void setTaskForce(ITaskForce taskForce) {
		this.taskForce = taskForce;
	}

	@Override
	public List<ITaskProcessing> getTaskProcessings() {
		if (taskProcessings == null) {
			return this.taskProcessings = new LinkedList<ITaskProcessing>();
		} else {
			return this.taskProcessings;
		}
	}

	@Override
	public void setTaskProcessings(List<ITaskProcessing> processings) {
		this.taskProcessings = processings;
	}

	@Override
	public void addTaskProcessing(ITaskProcessing processing) {
		getTaskProcessings().add(processing);
	}

}
