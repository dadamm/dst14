package dst.ass1.jpa.model;

import java.util.List;

public interface IUser {

	public String getUsername();

	public void setUsername(String username);

	public byte[] getPassword();

	public void setPassword(byte[] password);

	public String getAccountNo();

	public void setAccountNo(String accountNo);

	public String getBankCode();

	public void setBankCode(String bankCode);

	public List<ITask> getTasks();

	public void setTasks(List<ITask> tasks);

	public void addTask(ITask task);

	public void addMembership(IMembership membership);

	public List<IMembership> getMemberships();

	public void setMemberships(List<IMembership> memberships);

}
