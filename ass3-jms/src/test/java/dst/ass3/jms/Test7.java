package dst.ass3.jms;

import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.log;
import static dst.ass3.util.Utils.logCheckpoint;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dst.ass3.AbstractJMSTest;
import dst.ass3.dto.ProcessTaskWrapperDTO;
import dst.ass3.dto.RateTaskWrapperDTO;
import dst.ass3.dto.TaskWrapperDTO;
import dst.ass3.jms.scheduler.IScheduler.ISchedulerListener;
import dst.ass3.jms.taskforce.ITaskForce.ITaskForceListener;
import dst.ass3.jms.taskworker.ITaskWorker.ITaskWorkerListener;
import dst.ass3.model.Complexity;
import dst.ass3.model.LifecycleState;

/**
 * This test performs the following tasks: Assign 3 Tasks, accept and process
 * all of them.
 * 
 * <pre>
 * Timing diagram
 * 
 *      0  1  2  3  4  5  6  7  8  9  10 [sec]                                     
 *      |--|--|--|--|--|--|--|--|--|--|-->                                         
 * T1/2 ******************                                                         
 * T3         ******************                                                   
 *      ^     ^     ^        ^        ^                                            
 *      CP1   CP2   CP3      CP4      CP5                                           
 *                                                                                 
 * Each Task needs ****************** (=6sec) for processing                        
 * CP1: Check-Point 1 - Assign task1, task2                                                           
 * CP2: Check-Point 2 - Assign task3                                                                 
 * CP3: Check-Point 3 - 1-3 running                                                                 
 * CP4: Check-Point 4 - 1-2 PROCESSED, 3 running                                                    
 * CP5: Check-Point 5 - 3 PROCESSED
 * </pre>
 */
public class Test7 extends AbstractJMSTest {

	private AtomicInteger taskForceEventTask1 = new AtomicInteger(0);
	private AtomicInteger taskForceEventTask2 = new AtomicInteger(0);
	private AtomicInteger taskForceEventTask3 = new AtomicInteger(0);

	private AtomicInteger schedulerEvent = new AtomicInteger(0);
	private AtomicInteger schedulerEventTask1 = new AtomicInteger(0);
	private AtomicInteger schedulerEventTask2 = new AtomicInteger(0);
	private AtomicInteger schedulerEventTask3 = new AtomicInteger(0);

	private AtomicInteger taskWorkerEvent = new AtomicInteger(0);
	private AtomicInteger taskWorkerEventTask1 = new AtomicInteger(0);
	private AtomicInteger taskWorkerEventTask2 = new AtomicInteger(0);
	private AtomicInteger taskWorkerEventTask3 = new AtomicInteger(0);

	private long taskId1 = 70;
	private long taskId2 = 71;
	private long taskId3 = 72;
	private long taskWrapperId1 = -1;
	private long taskWrapperId2 = -1;
	private long taskWrapperId3 = -1;

	private String task1RatedBy;
	private String task2RatedBy;
	private String task3RatedBy;
	private long startTime;

	private final int PROCESSING_TIME = 6000;

	private Semaphore sem;
	private Semaphore semWorker;

	@Before
	public void init() {
		super.init();
	}

	@Test
	public void test_ComplexAssignRateAndProcess() {
		sem = new Semaphore(0);
		semWorker = new Semaphore(0);

		tf1.start();
		tf2.start();
		scheduler.start();
		tw1.start();
		tw2.start();
		tw3.start();
		tw4.start();

		ITaskForceListener taskForceListener = new ITaskForceListener() {
			@Override
			public TaskWrapperDecideResponse decideTask(RateTaskWrapperDTO taskWrapper,
					String taskForceName) {
				logTimed("** taskForce " + taskForceName + " task: " + taskWrapper,
						startTime);
				if (taskWrapper.getTaskId().longValue() == taskId1) {
					taskForceEventTask1.incrementAndGet();
					taskWrapperId1 = taskWrapper.getId();
					task1RatedBy = taskForceName;
					return new TaskWrapperDecideResponse(TaskWrapperResponse.ACCEPT,
							Complexity.EASY);
				}
				if (taskWrapper.getTaskId().longValue() == taskId2) {
					taskForceEventTask2.incrementAndGet();
					taskWrapperId2 = taskWrapper.getId();
					task2RatedBy = taskForceName;
					return new TaskWrapperDecideResponse(TaskWrapperResponse.ACCEPT,
							Complexity.CHALLENGING);
				}
				if (taskWrapper.getTaskId().longValue() == taskId3) {
					taskForceEventTask3.incrementAndGet();
					taskWrapperId3 = taskWrapper.getId();
					task3RatedBy = taskForceName;
					return new TaskWrapperDecideResponse(TaskWrapperResponse.ACCEPT,
							Complexity.CHALLENGING);
				}

				fail("taskForce Events - unknown type");
				return new TaskWrapperDecideResponse(TaskWrapperResponse.DENY, null);
			}
		};

		ITaskWorkerListener taskWorkerListener = new ITaskWorkerListener() {
			@Override
			public void waitTillProcessed(ProcessTaskWrapperDTO taskWrapper,
					String taskWorkerName, Complexity acceptedComplexity,
					String taskForceName) {
				logTimed("** taskWorker " + taskWorkerName + " task: " + taskWrapper,
						startTime);

				if (taskWrapper.getId().longValue() == taskWrapperId1) {
					taskWorkerEventTask1.incrementAndGet();
					assertEquals("taskWorkerListener 1 taskWrapperId", taskWrapperId1, taskWrapper
							.getId().longValue());
					assertEquals("taskWorkerListener 1 taskId", taskId1, taskWrapper
							.getTaskId().longValue());
					assertEquals("taskWorkerListener 1 complexity",
							Complexity.EASY, taskWrapper.getComplexity());
					assertEquals("taskWorkerListener 1 ratedby", task1RatedBy,
							taskWrapper.getRatedBy());
				}
				if (taskWrapper.getId().longValue() == taskWrapperId2) {
					taskWorkerEventTask2.incrementAndGet();
					assertEquals("taskWorkerListener 2 taskWrapperId", taskWrapperId2, taskWrapper
							.getId().longValue());
					assertEquals("taskWorkerListener 2 taskId", taskId2, taskWrapper
							.getTaskId().longValue());
					assertEquals("taskWorkerListener 2 complexity",
							Complexity.CHALLENGING, taskWrapper.getComplexity());
					assertEquals("taskWorkerListener 2 ratedby", task2RatedBy,
							taskWrapper.getRatedBy());
				}
				if (taskWrapper.getId().longValue() == taskWrapperId3) {
					taskWorkerEventTask3.incrementAndGet();
					assertEquals("taskWorkerListener 3 taskWrapperId", taskWrapperId3, taskWrapper
							.getId().longValue());
					assertEquals("taskWorkerListener 3 taskId", taskId3, taskWrapper
							.getTaskId().longValue());
					assertEquals("taskWorkerListener 3 complexity",
							Complexity.CHALLENGING, taskWrapper.getComplexity());
					assertEquals("taskWorkerListener 3 ratedby", task3RatedBy,
							taskWrapper.getRatedBy());
				}

				taskWorkerEvent.incrementAndGet();
				sem.release();

				sleep(PROCESSING_TIME);

				semWorker.release();
				logTimed("finish " + taskWorkerName, startTime);
			}
		};

		ISchedulerListener schedulerListener = new ISchedulerListener() {
			@Override
			public void notify(InfoType type, TaskWrapperDTO taskWrapper) {
				logTimed("** scheduler: type=" + type + " task: " + taskWrapper,
						startTime);
				sleep(SHORT_WAIT); // wait short time for updated taskWrapperId
				if (taskWrapper.getTaskId().longValue() == taskId1) {
					assertEquals("taskWrapperId in server response DTO wrong"
							+ schedulerEvent, taskWrapperId1, taskWrapper.getId().longValue());
					schedulerEventTask1.incrementAndGet();
				}
				if (taskWrapper.getTaskId().longValue() == taskId2) {
					assertEquals("taskWrapperId in server response DTO wrong Task2 "
							+ schedulerEvent, taskWrapperId2, taskWrapper.getId().longValue());
					schedulerEventTask2.incrementAndGet();
				}
				if (taskWrapper.getTaskId().longValue() == taskId3) {
					assertEquals("taskWrapperId in server response DTO wrong Task3 "
							+ schedulerEvent, taskWrapperId3, taskWrapper.getId().longValue());
					schedulerEventTask3.incrementAndGet();
				}

				switch (schedulerEvent.get()) {
				case 0:
					// ASSIGN for taskId1 / taskId2
					assertEquals("1st event of wrong type", InfoType.CREATED,
							type);
					assertEquals("1st event wrong", LifecycleState.ASSIGNED,
							taskWrapper.getState());
					assertEquals("1st event complexity wrong",
							Complexity.UNRATED, taskWrapper.getComplexity());
					break;
				case 1:
					// ASSIGN for taskId1 / taskId2
					assertEquals("2nd event of wrong type", InfoType.CREATED,
							type);
					assertEquals("2nd event wrong", LifecycleState.ASSIGNED,
							taskWrapper.getState());
					assertEquals("2nd event complexity wrong",
							Complexity.UNRATED, taskWrapper.getComplexity());
					break;
				case 2:
					// ASSIGN for taskId3
					assertEquals("3rd event of wrong type", InfoType.CREATED,
							type);
					assertEquals("3rd event wrong", LifecycleState.ASSIGNED,
							taskWrapper.getState());
					assertEquals("3rd event complexity wrong",
							Complexity.UNRATED, taskWrapper.getComplexity());
					break;
				case 3:
					// PROCESSED 1-2
					assertEquals("4th event of wrong type", InfoType.PROCESSED,
							type);
					assertEquals("4th event wrong", LifecycleState.PROCESSED,
							taskWrapper.getState());
					assertNotSame("4th event complexity wrong",
							Complexity.UNRATED, taskWrapper.getComplexity());
					break;
				case 4:
					// PROCESSED 1-2
					assertEquals("5th event of wrong type", InfoType.PROCESSED,
							type);
					assertEquals("5th event wrong", LifecycleState.PROCESSED,
							taskWrapper.getState());
					assertNotSame("5th event complexity wrong",
							Complexity.UNRATED, taskWrapper.getComplexity());
					break;
				case 5:
					// PROCESSED 3
					assertEquals("6th event of wrong type", InfoType.PROCESSED,
							type);
					assertEquals("6th wrong task", taskId3, taskWrapper.getTaskId()
							.longValue());
					assertEquals("6th event wrong", LifecycleState.PROCESSED,
							taskWrapper.getState());
					assertEquals("6th event complexity ",
							Complexity.CHALLENGING, taskWrapper.getComplexity());
					assertEquals("6th event rated by", task3RatedBy,
							taskWrapper.getRatedBy());
					break;
				default:
					fail("only 6 events expected");
					break;
				}

				schedulerEvent.incrementAndGet();

				sem.release();
			}
		};

		sleep(SHORT_WAIT); // Wait for old messages being discarded.

		startTime = new Date().getTime();
		// ---------------- CP1 ------------------------
		logCheckpoint(1, startTime);

		tf1.setTaskForceListener(taskForceListener);
		tf2.setTaskForceListener(taskForceListener);

		tw1.setTaskWorkerListener(taskWorkerListener);
		tw2.setTaskWorkerListener(taskWorkerListener);
		tw3.setTaskWorkerListener(taskWorkerListener);
		tw4.setTaskWorkerListener(taskWorkerListener);

		scheduler.setSchedulerListener(schedulerListener);

		log("Assigning " + taskId1 + "...");
		scheduler.assign(taskId1);
		log("Assigning " + taskId2 + "...");
		scheduler.assign(taskId2);

		assure(sem,
				4,
				"did not receive 4 events (Scheduler: create, create, taskWorker: pre-processing, pre-processing) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP2 ------------------------
		logCheckpoint(2, startTime);

		assertEquals("wrong count of scheduler events ", 2,
				schedulerEvent.get());
		assertEquals("wrong count of scheduler 1 events ", 1,
				schedulerEventTask1.get());
		assertEquals("wrong count of scheduler 2 events ", 1,
				schedulerEventTask2.get());
		assertEquals("wrong count of taskWorker 1 events ", 1,
				taskWorkerEventTask1.get());
		assertEquals("wrong count of taskWorker 2 events ", 1,
				taskWorkerEventTask2.get());
		assertEquals("wrong count of taskWorker 3 events ", 0,
				taskWorkerEventTask3.get());

		sleep(2000); // 2 sec delayed startup
		scheduler.assign(taskId3);

		assure(sem, 2, "did not receive 2 event (Scheduler: create, taskWorker: pre-processing) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP3 ------------------------
		logCheckpoint(3, startTime);

		assertEquals("wrong count of taskWorker events ", 3, taskWorkerEvent.get());

		assertEquals("wrong count of scheduler events ", 3,
				schedulerEvent.get());
		assertEquals("wrong count of scheduler 1 events ", 1,
				schedulerEventTask1.get());
		assertEquals("wrong count of scheduler 2 events ", 1,
				schedulerEventTask2.get());
		assertEquals("wrong count of scheduler 3 events ", 1,
				schedulerEventTask3.get());
		assertEquals("wrong count of taskWorker 1 events ", 1,
				taskWorkerEventTask1.get());
		assertEquals("wrong count of taskWorker 2 events ", 1,
				taskWorkerEventTask2.get());
		assertEquals("wrong count of taskWorker 3 events ", 1,
				taskWorkerEventTask3.get());

		// Task 1 and 2 need some time to finish
		assure(semWorker,
				2,
				"did not receive 2 events (taskWorker: finished processing, finished processing) in time",
				DEFAULT_CHECK_TIMEOUT + PROCESSING_TIME / 1000);
		assure(sem,
				2,
				"did not receive 2 events (Scheduler: processed, processed) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP4 ------------------------
		logCheckpoint(4, startTime);

		assertEquals("wrong count of scheduler events ", 5,
				schedulerEvent.get());
		assertEquals("wrong count of scheduler 1 events ", 2,
				schedulerEventTask1.get());
		assertEquals("wrong count of scheduler 2 events ", 2,
				schedulerEventTask2.get());
		assertEquals("wrong count of scheduler 3 events ", 1,
				schedulerEventTask3.get());

		assertEquals("wrong count of taskForce Task1 events ", 1,
				taskForceEventTask1.get());
		assertEquals("wrong count of taskForce Task2 events ", 1,
				taskForceEventTask2.get());
		assertEquals("wrong count of taskForce Task3 events ", 1,
				taskForceEventTask3.get());

		assertEquals("wrong count of taskWorker events ", 3, taskWorkerEvent.get());
		assertEquals("wrong count of taskWorker 1 events ", 1,
				taskWorkerEventTask1.get());
		assertEquals("wrong count of taskWorker 2 events ", 1,
				taskWorkerEventTask2.get());
		assertEquals("wrong count of taskWorker 3 events ", 1,
				taskWorkerEventTask3.get());

		assure(semWorker,
				1,
				"did not receive 1 event (taskWorker: finished processing) in time",
				DEFAULT_CHECK_TIMEOUT + PROCESSING_TIME / 1000);

		assure(sem, 1,
				"did not receive 1 event (Scheduler: processed) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP5 ------------------------
		logCheckpoint(5, startTime);
		assertEquals("wrong count of scheduler events ", 6,
				schedulerEvent.intValue());
		assertEquals("wrong count of scheduler 1 events ", 2,
				schedulerEventTask1.get());
		assertEquals("wrong count of scheduler 2 events ", 2,
				schedulerEventTask2.get());
		assertEquals("wrong count of scheduler 3 events ", 2,
				schedulerEventTask3.get());

		assertEquals("wrong count of taskForce Task1 events ", 1,
				taskForceEventTask1.get());
		assertEquals("wrong count of taskForce Task2 events ", 1,
				taskForceEventTask2.get());
		assertEquals("wrong count of taskForce Task3 events ", 1,
				taskForceEventTask3.get());

		assertEquals("wrong count of taskWorker events ", 3, taskWorkerEvent.get());
		assertEquals("wrong count of taskWorker 1 events ", 1,
				taskWorkerEventTask1.get());
		assertEquals("wrong count of taskWorker 2 events ", 1,
				taskWorkerEventTask2.get());
		assertEquals("wrong count of taskWorker 3 events ", 1,
				taskWorkerEventTask3.get());

	}

	@After
	public void shutdown() {
		// disable all listeners
		tf1.setTaskForceListener(null);
		tf2.setTaskForceListener(null);
		scheduler.setSchedulerListener(null);

		log("shutting down tw1-4...");
		tw1.stop();
		tw2.stop();
		tw3.stop();
		tw4.stop();
		log("shutting down tf1...");
		tf1.stop();
		log("shutting down tf2...");
		tf2.stop();
		log("shutting down S...");
		scheduler.stop();

		super.shutdown();
	}

}
