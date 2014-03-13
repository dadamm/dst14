package dst.ass1.jpa.model.impl;

import java.util.List;

import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.ITaskForce;

public class Expert extends AbstractPerson implements IExpert {

	@Override
	public List<ITaskForce> getAdvisedTaskForces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAdvisedTaskForces(List<ITaskForce> taskForces) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAdvisedTaskForce(ITaskForce taskForce) {
		// TODO Auto-generated method stub

	}

}
