package dst.ass1.jpa.model.impl;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;

import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.util.Constants;

@Entity
@Table(name = Constants.T_USER, uniqueConstraints = @UniqueConstraint(columnNames = {"accountno", "bankcode"}))
public class User extends AbstractPerson implements IUser {

	@Column(name = "username", length = 50, unique = true, nullable = false)
	private String username;
	
	@Column(name = "password", length = 16)
	@Index(name = "password")
	private byte[] password;
	
	@Column(name = "accountno", length = 30)
	private String accountNo;
	
	@Column(name = "bankcode", length = 11)
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
		if (tasks == null) {
			return this.tasks = new LinkedList<ITask>();
		} else {
			return this.tasks;
		}
	}

	@Override
	public void setTasks(List<ITask> tasks) {
		this.tasks = tasks;
	}

	@Override
	public void addTask(ITask task) {
		getTasks().add(task);
	}

	@Override
	public void addMembership(IMembership membership) {
		getMemberships().add(membership);
	}

	@Override
	public List<IMembership> getMemberships() {
		if (memberships == null) {
			return this.memberships = new LinkedList<IMembership>();
		} else {
			return this.memberships;
		}
	}

	@Override
	public void setMemberships(List<IMembership> memberships) {
		this.memberships = memberships;
	}

}
