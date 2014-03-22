package dst.ass1.jpa.model;

import java.io.Serializable;

import dst.ass1.jpa.model.impl.Address;
import dst.ass1.jpa.model.impl.Expert;
import dst.ass1.jpa.model.impl.Membership;
import dst.ass1.jpa.model.impl.MembershipKey;
import dst.ass1.jpa.model.impl.Metadata;
import dst.ass1.jpa.model.impl.Task;
import dst.ass1.jpa.model.impl.TaskForce;
import dst.ass1.jpa.model.impl.TaskProcessing;
import dst.ass1.jpa.model.impl.TaskWorker;
import dst.ass1.jpa.model.impl.User;
import dst.ass1.jpa.model.impl.WorkPlatform;

public class ModelFactory implements Serializable {

	private static final long serialVersionUID = 1L;

	public IAddress createAddress() {
		return new Address();
	}

	public IExpert createExpert() {
		return new Expert();
	}

	public ITaskForce createTaskForce() {
		return new TaskForce();
	}

	public ITaskWorker createTaskWorker() {
		return new TaskWorker();
	}

	public IMetadata createMetadata() {
		return new Metadata();
	}

	public ITaskProcessing createTaskProcessing() {
		return new TaskProcessing();
	}

	public IWorkPlatform createPlatform() {
		return new WorkPlatform();
	}

	public ITask createTask() {
		return new Task();
	}

	public IMembership createMembership() {
		return new Membership();
	}

	public IMembershipKey createMembershipKey() {
		return new MembershipKey();
	}

	public IUser createUser() {
		return new User();
	}

}
