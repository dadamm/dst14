package dst.ass1.jpa.model.impl;

import java.util.List;

import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.ITaskForce;

public class Expert extends AbstractPerson implements IExpert {
	
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
