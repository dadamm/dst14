package dst.ass3.jms.scheduler;

import dst.ass3.dto.TaskWrapperDTO;

public interface IScheduler {
	/**
	 * Starts a scheduler
	 */
	void start();

	/**
	 * Stops the scheduler and cleans all resources (e.g.: close session,
	 * connection, etc.).
	 */
	void stop();

	/**
	 * Assigns a new Task with the given ID.
	 * 
	 * @param taskId
	 */
	void assign(long taskId);

	/**
	 * Requests information for the given TaskWrapper ID.
	 * 
	 * @param taskWrapperId
	 */
	void info(long taskWrapperId);

	/**
	 * Sets the listener to report incoming messages. Only one Listener should
	 * be in use at any time. Be sure to handle cases where messages are
	 * received but not listener is yet set (discard the message).
	 * 
	 * @param listener
	 */
	void setSchedulerListener(ISchedulerListener listener);

	interface ISchedulerListener {
		enum InfoType {
			CREATED, INFO, PROCESSED, DENIED
		}

		/**
		 * Notifies the listener about an incoming message.
		 * 
		 * @param type
		 *            the type of the incoming message
		 * @param taskWrapper
		 *            the task of the incoming message
		 */
		void notify(InfoType type, TaskWrapperDTO taskWrapper);
	}
}
