package dst.ass3.jms.scheduler.impl;

import dst.ass3.jms.scheduler.IScheduler;

public class Scheduler implements IScheduler {
	
	private ISchedulerListener schedulerListener;
	
	public Scheduler() {
		
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
	public void assign(long taskId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void info(long taskWrapperId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSchedulerListener(ISchedulerListener listener) {
		this.schedulerListener = listener;
	}

}
