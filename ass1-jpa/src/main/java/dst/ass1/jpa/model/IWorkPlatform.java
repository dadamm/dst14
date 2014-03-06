package dst.ass1.jpa.model;

import java.math.BigDecimal;
import java.util.List;

public interface IWorkPlatform {

	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public String getLocation();

	public void setLocation(String location);

	public BigDecimal getCostsPerWorkUnit();

	public void setCostsPerWorkUnit(BigDecimal costsPerWorkUnit);

	public void addMembership(IMembership membership);

	public List<IMembership> getMemberships();

	public void setMemberships(List<IMembership> memberships);

	public List<ITaskForce> getTaskForces();

	public void setTaskForces(List<ITaskForce> taskForces);

	public void addTaskForce(ITaskForce taskForce);
}
