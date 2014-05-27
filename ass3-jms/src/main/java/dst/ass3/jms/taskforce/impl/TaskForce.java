package dst.ass3.jms.taskforce.impl;

import dst.ass3.jms.taskforce.ITaskForce;

public class TaskForce implements ITaskForce {
	
	private ITaskForceListener taskForceListener;
	
	public TaskForce(String name) {
		
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
	public void setTaskForceListener(ITaskForceListener listener) {
		this.taskForceListener = listener;
	}

}
