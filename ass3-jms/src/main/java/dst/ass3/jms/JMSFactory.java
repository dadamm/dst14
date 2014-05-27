package dst.ass3.jms;

import dst.ass3.jms.scheduler.IScheduler;
import dst.ass3.jms.scheduler.impl.Scheduler;
import dst.ass3.jms.taskforce.ITaskForce;
import dst.ass3.jms.taskforce.impl.TaskForce;
import dst.ass3.jms.taskworker.ITaskWorker;
import dst.ass3.jms.taskworker.impl.TaskWorker;
import dst.ass3.model.Complexity;

public class JMSFactory {
	
	public static ITaskForce createTaskForce(String name) {
		return new TaskForce(name);
	}

	public static ITaskWorker createTaskWorker(String name, String taskForce, Complexity complexity) {
		return new TaskWorker(name, taskForce, complexity);
	}

	public static IScheduler createScheduler() {
		return new Scheduler();
	}

}
