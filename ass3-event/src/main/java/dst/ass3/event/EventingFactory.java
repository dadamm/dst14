package dst.ass3.event;

import dst.ass3.model.ITaskWrapper;
import dst.ass3.model.Complexity;
import dst.ass3.model.LifecycleState;
import dst.ass3.dto.TaskWrapperDTO;
import dst.ass3.event.impl.EventProcessing;

/**
 * Factory for instantiating objects used in the eventing tests
 * (interfaces IEventProcessing and ITask).
 */
public class EventingFactory {


	public static IEventProcessing getInstance() {
		return new EventProcessing();
	}

	public static ITaskWrapper createTaskWrapper(Long id, Long taskId, LifecycleState state,
			String ratedBy, Complexity complexity) {
		return new TaskWrapperDTO(id, taskId, state, ratedBy, complexity);
	}
}
