package dst.ass3.event.impl;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.StatementAwareUpdateListener;

import dst.ass3.event.Constants;
import dst.ass3.event.IEventProcessing;
import dst.ass3.model.ITaskWrapper;

public class EventProcessing implements IEventProcessing {
	
	private EPServiceProvider serviceProvider;
	
	private String createTaskAssignedTypeString() {
		StringBuilder sb = new StringBuilder();
		sb.append("create schema ");
		sb.append(Constants.EVENT_TASK_ASSIGNED);
		sb.append(" (taskwrapper dst.ass3.model.TaskWrapper)");
		return sb.toString();
	}

	private String createTaskProcessedTypeString() {
		StringBuilder sb = new StringBuilder();
		sb.append("create schema ");
		sb.append(Constants.EVENT_TASK_PROCESSED);
		sb.append(" (taskwrapper dst.ass3.model.TaskWrapper)");
		return sb.toString();
	}
	
	private String createTaskDurationTypeString() {
		StringBuilder sb = new StringBuilder();
		sb.append("create schema ");
		sb.append(Constants.EVENT_TASK_DURATION);
		sb.append(" (taskwrapper dst.ass3.model.TaskWrapper)");
		return sb.toString();
	}
	
	private Configuration getConfiguration(boolean debug) {
		Configuration config = new Configuration();
		if (debug) {
			config.getEngineDefaults().getLogging().setEnableExecutionDebug(true);
			config.getEngineDefaults().getLogging().setEnableTimerDebug(false);
			config.getEngineDefaults().getLogging().setEnableQueryPlan(false);
		}
		return config;
	}

	@Override
	public void initializeAll(StatementAwareUpdateListener listener, boolean debug) {
		serviceProvider = EPServiceProviderManager.getProvider("EsperEngineDST", getConfiguration(debug));
		
		EPAdministrator administrator = serviceProvider.getEPAdministrator();
		administrator.createEPL(createTaskAssignedTypeString());
		administrator.createEPL(createTaskProcessedTypeString());
		administrator.createEPL(createTaskDurationTypeString());
	}

	@Override
	public void addEvent(ITaskWrapper taskWrapper) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
