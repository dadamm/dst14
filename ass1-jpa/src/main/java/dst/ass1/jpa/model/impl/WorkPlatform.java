package dst.ass1.jpa.model.impl;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.util.Constants;

@Entity
@Table(name = Constants.T_WORKPLATFORM)
public class WorkPlatform implements IWorkPlatform {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "name", length = 50, unique = true)
	private String name;
	
	@Column(name = "location", length = 50)
	private String location;
	
	@Column(name = "costsperworkunit")
	private BigDecimal costsPerWorkUnit;
	
	@OneToMany(mappedBy = "workPlatform", targetEntity = Membership.class)
	private List<IMembership> memberships;
	
	@OneToMany(mappedBy = "workPlatform", targetEntity = TaskForce.class)
	private List<ITaskForce> taskForces;

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
	public String getLocation() {
		return this.location;
	}

	@Override
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public BigDecimal getCostsPerWorkUnit() {
		return this.costsPerWorkUnit;
	}

	@Override
	public void setCostsPerWorkUnit(BigDecimal costsPerWorkUnit) {
		this.costsPerWorkUnit = costsPerWorkUnit;
	}

	@Override
	public void addMembership(IMembership membership) {
		getMemberships().add(membership);
	}

	@Override
	public List<IMembership> getMemberships() {
		if (memberships == null) {
			return this.memberships = new LinkedList<IMembership>();
		} else {
			return this.memberships;
		}
	}

	@Override
	public void setMemberships(List<IMembership> memberships) {
		this.memberships = memberships;
	}

	@Override
	public List<ITaskForce> getTaskForces() {
		if (taskForces == null) {
			return this.taskForces = new LinkedList<ITaskForce>();
		} else {
			return this.taskForces;
		}
	}

	@Override
	public void setTaskForces(List<ITaskForce> taskForces) {
		this.taskForces = taskForces;
	}

	@Override
	public void addTaskForce(ITaskForce taskForce) {
		getTaskForces().add(taskForce);
	}

}
