package dst.ass3.jms.taskworker;

import dst.ass3.dto.ProcessTaskWrapperDTO;
import dst.ass3.model.Complexity;

public interface ITaskWorker {
	/**
	 * Starts a TaskWorker
	 */
	void start();

	/**
	 * Stops the TaskWorker and cleans all resources (e.g.: close session,
	 * connection, etc.).
	 */
	void stop();

	/**
	 * Sets the Listener. This listener simulates the execution of the
	 * given task wrapper. Only one Listener should be in use at any time. Be sure to
	 * handle cases where messages are received but no listener is yet set
	 * (discard the message).
	 * 
	 * @param listener
	 */
	void setTaskWorkerListener(ITaskWorkerListener listener);

	interface ITaskWorkerListener {
		/**
		 * Waits until the given Task wrapper has been processed. You should call this
		 * method ASYNC (in a new Thread) because it may return after a long
		 * time.
		 * 
		 * @param taskWrapper
		 *            the task to simulate execution
		 * @param taskWorkerName
		 *            the name of the TaskWorker calling this listener
		 * @param acceptedComplexity
		 *            the complexity this TaskWorker accepts
		 * @param taskForceName
		 *            the name of the TaskForce this TaskWorker belongs too
		 */
		void waitTillProcessed(ProcessTaskWrapperDTO taskWrapper, String taskWorkerName,
				Complexity acceptedComplexity, String taskForceName);
	}
}
