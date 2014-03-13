package dst.ass1.jpa.model.impl;

import java.util.List;

import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.IUser;

public class User extends AbstractPerson implements IUser {
	
	private String username;
	private byte[] password;
	private String accountNo;
	private String bankCode;
	private List<ITask> tasks;
	private List<IMembership> memberships;

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public byte[] getPassword() {
		return this.password;
	}

	@Override
	public void setPassword(byte[] password) {
		this.password = password;
	}

	@Override
	public String getAccountNo() {
		return this.accountNo;
	}

	@Override
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@Override
	public String getBankCode() {
		return this.bankCode;
	}

	@Override
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@Override
	public List<ITask> getTasks() {
		return this.tasks;
	}

	@Override
	public void setTasks(List<ITask> tasks) {
		this.tasks = tasks;
	}

	@Override
	public void addTask(ITask task) {
		this.tasks.add(task);
	}

	@Override
	public void addMembership(IMembership membership) {
		this.memberships.add(membership);
	}

	@Override
	public List<IMembership> getMemberships() {
		return this.memberships;
	}

	@Override
	public void setMemberships(List<IMembership> memberships) {
		this.memberships = memberships;
	}

}
