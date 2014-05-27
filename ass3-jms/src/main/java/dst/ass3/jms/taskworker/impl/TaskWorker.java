package dst.ass3.jms.taskworker.impl;

import dst.ass3.jms.taskworker.ITaskWorker;
import dst.ass3.jms.taskworker.ITaskWorker.ITaskWorkerListener;
import dst.ass3.model.Complexity;

public class TaskWorker implements ITaskWorker {
	
	private ITaskWorkerListener taskWorkerListener;
	
	public TaskWorker(String name, String taskForce, Complexity complexity) {
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTaskWorkerListener(ITaskWorkerListener listener) {
		this.taskWorkerListener = listener;
	}

}
