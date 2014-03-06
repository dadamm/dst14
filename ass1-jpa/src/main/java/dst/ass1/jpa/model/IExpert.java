package dst.ass1.jpa.model;

import java.util.List;

public interface IExpert {
	
	public List<ITaskForce> getAdvisedTaskForces();

	public void setAdvisedTaskForces(List<ITaskForce> taskForces);

	public void addAdvisedTaskForce(ITaskForce taskForce);

}
