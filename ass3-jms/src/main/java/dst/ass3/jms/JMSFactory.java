package dst.ass3.jms;

import dst.ass3.jms.scheduler.IScheduler;
import dst.ass3.jms.taskforce.ITaskForce;
import dst.ass3.jms.taskworker.ITaskWorker;
import dst.ass3.model.Complexity;

public class JMSFactory {
	
	public static ITaskForce createTaskForce(String name) {
		// TODO
		return null;
	}

	public static ITaskWorker createTaskWorker(String name, String taskForce,
			Complexity complexity) {
		// TODO
		return null;
	}

	public static IScheduler createScheduler() {
		// TODO
		return null;
	}

}
