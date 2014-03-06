package dst.ass1.jpa.model;

public interface IMembershipKey {

	public IUser getUser();

	public void setUser(IUser user);

	public IWorkPlatform getWorkPlatform();

	public void setWorkPlatform(IWorkPlatform platform);

}
