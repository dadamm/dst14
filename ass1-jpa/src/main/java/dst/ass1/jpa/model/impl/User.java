package dst.ass1.jpa.model.impl;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.util.Constants;

@Entity
@Table(name = Constants.T_USER)
public class User extends AbstractPerson implements IUser {

	@Column(name = "username", length = 50)
	private String username;
	
	@Column(name = "password", length = 16)
	private byte[] password;
	
	@Column(name = "accountno", length = 6)
	private String accountNo;
	
	@Column(name = "bankcode", length = 30)
	private String bankCode;
	
	@OneToMany(mappedBy = "user", targetEntity = Task.class)
	private List<ITask> tasks;
	
	@OneToMany(mappedBy = "user", targetEntity = Membership.class)
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
