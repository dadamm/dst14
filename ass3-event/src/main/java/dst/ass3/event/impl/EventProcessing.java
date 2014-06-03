package dst.ass3.event.impl;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.StatementAwareUpdateListener;

import dst.ass3.dto.TaskWrapperDTO;
import dst.ass3.event.Constants;
import dst.ass3.event.IEventProcessing;
import dst.ass3.model.ITaskWrapper;
import dst.ass3.model.LifecycleState;

public class EventProcessing implements IEventProcessing {
	
	private EPServiceProvider serviceProvider;
	
	private String createTaskTypeString(String taskType) {
		StringBuilder sb = new StringBuilder();
		sb.append("create schema ");
		sb.append(taskType);
		sb.append(" (taskId long, timestamp long)");
		return sb.toString();
	}

	private String createTaskDurationTypeString() {
		StringBuilder sb = new StringBuilder();
		sb.append("create schema ");
		sb.append(Constants.EVENT_TASK_DURATION);
		sb.append(" (taskId long, duration long)");
		return sb.toString();
	}
	
	private String createTaskAvgDurationTypeString() {
		StringBuilder sb = new StringBuilder();
		sb.append("create schema ");
		sb.append(Constants.EVENT_AVG_TASK_DURATION);
		sb.append(" (");
		sb.append(Constants.EVENT_AVG_TASK_DURATION);
		sb.append(" double )");
		return sb.toString();
	}
	
	private String getSelectStatement(String eventType) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ");
		sb.append(eventType);
		return sb.toString();
	}
	
	private Configuration getConfiguration(boolean debug) {
		Configuration config = new Configuration();
		config.addEventType(Constants.EVENT_TASK, TaskWrapperDTO.class);
		config.addImport(LifecycleState.class);
		if (debug) {
			config.getEngineDefaults().getLogging().setEnableExecutionDebug(true);
			config.getEngineDefaults().getLogging().setEnableTimerDebug(false);
			config.getEngineDefaults().getLogging().setEnableQueryPlan(false);
		}
		return config;
	}
	
	private String getTaskQuery(String eventType, LifecycleState state) {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into ");
		sb.append(eventType);
		sb.append(" (taskId, timestamp) ");
		sb.append("select taskId, current_timestamp() from ");
		sb.append(Constants.EVENT_TASK);
		sb.append(" where state = LifecycleState.");
		sb.append(state.toString());
		return sb.toString();
	}
	
	private String getNotifyTaskDurationQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into ");
		sb.append(Constants.EVENT_TASK_DURATION);
		sb.append("(taskId, duration)");
		sb.append(" select a.taskId, b.timestamp - a.timestamp from ");
		sb.append(Constants.EVENT_TASK_ASSIGNED);
		sb.append(".win:length(10000) a, ");
		sb.append(Constants.EVENT_TASK_PROCESSED);
		sb.append(".win:length(10000) b where a.taskId = b.taskId");
		return sb.toString();
	}
	
	private String getAvgDurationQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into ");
		sb.append(Constants.EVENT_AVG_TASK_DURATION);
		sb.append("(");
		sb.append(Constants.EVENT_AVG_TASK_DURATION);
		sb.append(") select avg(duration) from ");
		sb.append(Constants.EVENT_TASK_DURATION);
		sb.append(".win:time(15 seconds)");
		return sb.toString();
	}
	
	private String getFaildExecuteTaskQuery() {
		StringBuilder sb = new StringBuilder();
		
		String[] states = new String[] {"LifecycleState.READY_FOR_PROCESSING", "LifecycleState.PROCESSING_NOT_POSSIBLE"};
		for(int i = 0; i < 6 ; i++) {
			if(i == 0) {
				sb.append("every task=");
			}
			sb.append(Constants.EVENT_TASK);
			sb.append("(state=");
			sb.append(states[i%2]);
			if(i != 0) {
				sb.append(", id=task.id");
			}
			sb.append(") @consume(");
			sb.append(i+1);
			sb.append(")");
			if(i != 5) {
				sb.append(" -> ");
			}
		}
		return sb.toString();
	}

	@Override
	public void initializeAll(StatementAwareUpdateListener listener, boolean debug) {
		serviceProvider = EPServiceProviderManager.getProvider("EsperEngineDST", getConfiguration(debug));
		
		EPAdministrator administrator = serviceProvider.getEPAdministrator();
		administrator.createEPL(createTaskTypeString(Constants.EVENT_TASK_ASSIGNED));
		administrator.createEPL(getSelectStatement(Constants.EVENT_TASK_ASSIGNED)).addListener(listener);
		administrator.createEPL(createTaskTypeString(Constants.EVENT_TASK_PROCESSED));
		administrator.createEPL(getSelectStatement(Constants.EVENT_TASK_PROCESSED)).addListener(listener);
		administrator.createEPL(createTaskDurationTypeString());
		administrator.createEPL(getSelectStatement(Constants.EVENT_TASK_DURATION)).addListener(listener);
		administrator.createEPL(createTaskAvgDurationTypeString());
		administrator.createEPL(getSelectStatement(Constants.EVENT_AVG_TASK_DURATION)).addListener(listener);
		
		administrator.createEPL(getTaskQuery(Constants.EVENT_TASK_ASSIGNED, LifecycleState.ASSIGNED));
		administrator.createEPL(getTaskQuery(Constants.EVENT_TASK_PROCESSED, LifecycleState.PROCESSED));
		administrator.createEPL(getNotifyTaskDurationQuery());
		administrator.createEPL(getAvgDurationQuery());
		administrator.createPattern(getFaildExecuteTaskQuery()).addListener(listener);
	}

	@Override
	public void addEvent(ITaskWrapper taskWrapper) {
		serviceProvider.getEPRuntime().sendEvent(new TaskWrapperDTO(taskWrapper));
	}

	@Override
	public void close() {
		this.serviceProvider.destroy();
	}

}
