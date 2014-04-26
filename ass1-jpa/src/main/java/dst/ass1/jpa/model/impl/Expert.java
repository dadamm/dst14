package dst.ass1.jpa.model.impl;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.util.Constants;

//@NamedQuery(
//		name = "taskforcesOfExpert",
//		query = "select e from Expert e where firstname like 'Alex%'"
//)

@Entity
@Table(name = Constants.T_EXPERT)
public class Expert extends AbstractPerson implements IExpert {
	
	@OneToMany(mappedBy = "expert", targetEntity = TaskForce.class)
	private List<ITaskForce> advisedTaskForces;

	@Override
	public List<ITaskForce> getAdvisedTaskForces() {
		if (advisedTaskForces == null) {
			return this.advisedTaskForces = new LinkedList<ITaskForce>();
		} else {
			return this.advisedTaskForces;
		}
	}

	@Override
	public void setAdvisedTaskForces(List<ITaskForce> taskForces) {
		this.advisedTaskForces = taskForces;
	}

	@Override
	public void addAdvisedTaskForce(ITaskForce taskForce) {
		getAdvisedTaskForces().add(taskForce);
	}

}
