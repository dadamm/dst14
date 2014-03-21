package dst.ass1.jpa.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.ITaskForce;

@Entity
@Table(name = "expert")
public class Expert extends AbstractPerson implements IExpert {
	
	@OneToMany(mappedBy = "expert", targetEntity = TaskForce.class)
	private List<ITaskForce> advisedTaskForces;

	@Override
	public List<ITaskForce> getAdvisedTaskForces() {
		return this.advisedTaskForces;
	}

	@Override
	public void setAdvisedTaskForces(List<ITaskForce> taskForces) {
		this.advisedTaskForces = taskForces;
	}

	@Override
	public void addAdvisedTaskForce(ITaskForce taskForce) {
		this.advisedTaskForces.add(taskForce);
	}

}
