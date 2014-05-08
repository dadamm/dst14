package dst.ass3.jms.taskforce;

import dst.ass3.dto.RateTaskWrapperDTO;
import dst.ass3.model.Complexity;

public interface ITaskForce {
	/**
	 * Starts a TaskForce
	 */
	void start();

	/**
	 * Stops the TaskForce and cleans all resources (e.g.: close session,
	 * connection, etc.). Keep in mind that Listeners may be sleeping when stop
	 * is requested. Be sure to interrupt them and discard the results they
	 * might return, because the system is stopping already.
	 */
	void stop();

	/**
	 * Sets the Listener. Only one Listener should be in use at any
	 * time. Be sure to handle cases where messages are received but no
	 * listener is yet set (discard the message). The listeners may block
	 * forever, so be sure to interrupt them in stop().
	 * 
	 * @param listener
	 */
	void setTaskForceListener(ITaskForceListener listener);

	interface ITaskForceListener {
		enum TaskWrapperResponse {
			ACCEPT, DENY
		};

		class TaskWrapperDecideResponse {
			public TaskWrapperResponse resp;
			public Complexity complexity;

			public TaskWrapperDecideResponse(TaskWrapperResponse resp,
					Complexity complexity) {
				this.resp = resp;
				this.complexity = complexity;
			}
		}

		/**
		 * Decide on the given task wrapper.
		 * 
		 * @param taskWrapper
		 *            the task to decide
		 * @param taskForceName
		 *            name the TaskForce executing this listener
		 * @return ACCEPT + Complexity | DENY
		 */
		TaskWrapperDecideResponse decideTask(RateTaskWrapperDTO taskWrapper, String taskForceName);
	}
}
