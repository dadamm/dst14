package dst.ass3.jms;

import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.log;
import static dst.ass3.util.Utils.logCheckpoint;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
 * This test performs the following tasks: ASSIGN, RATE as CHALLENGING and
 * PROCESS.
 * 
 * <pre>
 * Timing diagram
 * 
 *    0  1  2  3  4                                                                       
 *    |--|--|--|--|-->                                                                    
 * T1 *******                                                                             
 * I1          *                                                                           
 *    ^     ^  ^  ^                                                                       
 *    CP1   CP2+3 CP4                                                                      
 *                                                                                        
 * **: Running                                                                            
 *                                                                                        
 * T1: takes 2sec to finish                                                               
 * I1: InfoRequest for TaskWrapper1                                                              
 *                                                                                        
 * CP1: Check-Point 1 - Assign task1                                         
 * CP2: Check-Point 2 - Wait till task1 has finished                         
 * CP3: Check-Point 3 - task1 should have finished so send INFO Request                                                    
 * CP4: Check-Point 4 - Info Request completed [Scheduler = ASSIGN, PROCESSED, INFO events]
 * </pre>
 */
public class Test4 extends AbstractJMSTest {

	private AtomicInteger taskForceEvent = new AtomicInteger(0);
	private AtomicInteger schedulerEvent = new AtomicInteger(0);
	private AtomicInteger processed = new AtomicInteger(0);

	private long taskId1 = 40;
	private long taskWrapperId1 = -1;

	private String ratedBy = null;
	private long startTime;

	private Semaphore sem;
	private Semaphore semWorker;

	private final int PROCESSING_TIME = 2000;

	@Before
	public void init() {
		super.init();
	}

	@Test
	public void test_AssignRateAndProcess1() {
		sem = new Semaphore(0);
		semWorker = new Semaphore(0);
		tf1.start();
		scheduler.start();
		tw1.start();

		ITaskForceListener taskForceListener = new ITaskForceListener() {
			@Override
			public TaskWrapperDecideResponse decideTask(
					RateTaskWrapperDTO taskWrapper, String taskForceName) {
				logTimed("** taskForce " + taskForceName + " taskWrapper: "
						+ taskWrapper, startTime);

				assertNotNull("taskWrapper.taskId = null",
						taskWrapper.getTaskId());
				assertNotNull("taskWrapper.id = null", taskWrapper.getId());

				assertEquals("taskId wrong", taskId1, taskWrapper.getTaskId()
						.longValue());

				log("SETTING ID " + taskWrapper.getId());
				taskWrapperId1 = taskWrapper.getId();
				ratedBy = taskForceName;

				taskForceEvent.incrementAndGet();
				sem.release();

				return new TaskWrapperDecideResponse(
						TaskWrapperResponse.ACCEPT, Complexity.CHALLENGING);
			}
		};

		ITaskWorkerListener workerListener = new ITaskWorkerListener() {
			@Override
			public void waitTillProcessed(ProcessTaskWrapperDTO taskWrapper,
					String taskWorkerName, Complexity acceptedComplexity,
					String taskForceName) {
				logTimed("** taskWorker " + taskWorkerName + " taskWrapper: "
						+ taskWrapper, startTime);

				assertEquals("taskForceName", ratedBy, taskForceName);
				assertEquals("taskWorkerName", TASKWORKER1_NAME, taskWorkerName);
				assertEquals("taskcomplexity", Complexity.CHALLENGING,
						acceptedComplexity);

				sleep(PROCESSING_TIME); // simulate processing

				processed.incrementAndGet();

				semWorker.release();
				assertEquals("worker listener - too many events", 1,
						processed.get());
			}
		};

		ISchedulerListener schedulerListener = new ISchedulerListener() {
			@Override
			public void notify(InfoType type, TaskWrapperDTO taskWrapper) {
				logTimed("** scheduler: type=" + type + " taskWrapper: "
						+ taskWrapper, startTime);
				sleep(SHORT_WAIT); // wait short time for updated taskId

				assertEquals("taskId in server response DTO wrong " + taskId1,
						taskId1, taskWrapper.getTaskId().longValue());

				assertEquals("taskWrapperId in server response DTO wrong"
						+ schedulerEvent, taskWrapperId1, taskWrapper.getId()
						.longValue());

				switch (schedulerEvent.get()) {
				case 0:
					// ASSIGN for taskId1
					assertEquals("1st event of wrong type", InfoType.CREATED,
							type);
					assertEquals("1st event != ASSIGNED",
							LifecycleState.ASSIGNED, taskWrapper.getState());
					assertEquals("1st event complexity != UNRATED",
							Complexity.UNRATED, taskWrapper.getComplexity());
					break;
				case 1:
					// PROCESSED
					assertEquals("2nd event of wrong type", InfoType.PROCESSED,
							type);
					assertEquals("2nd event != ASSIGNED",
							LifecycleState.PROCESSED, taskWrapper.getState());
					assertEquals("2nd event complexity ",
							Complexity.CHALLENGING, taskWrapper.getComplexity());
					assertNotNull("2nd rated by == null " + ratedBy,
							taskWrapper.getRatedBy());
					assertEquals("2nd rated by != " + ratedBy, ratedBy,
							taskWrapper.getRatedBy());
					break;
				case 2:
					// INFO
					assertEquals("3rd event of wrong type", InfoType.INFO, type);
					assertEquals("3rd event != ASSIGNED",
							LifecycleState.PROCESSED, taskWrapper.getState());
					assertEquals("3rd event complexity ",
							Complexity.CHALLENGING, taskWrapper.getComplexity());
					assertNotNull("3rd rated by == null" + ratedBy,
							taskWrapper.getRatedBy());
					assertEquals("3rd rated by != " + ratedBy, ratedBy,
							taskWrapper.getRatedBy());
					break;
				default:
					fail("only 3 events expected");
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
		scheduler.setSchedulerListener(schedulerListener);
		tw1.setTaskWorkerListener(workerListener);

		log("Assigning " + taskId1 + "...");
		scheduler.assign(taskId1);

		assure(sem,
				2,
				"did not get 2 events (Scheduler: create; TaskForce: rate) in time",
				DEFAULT_CHECK_TIMEOUT);
		// ---------------- CP2 ------------------------
		logCheckpoint(2, startTime);

		assertEquals("wrong count of taskForce events ", 1,
				taskForceEvent.get());
		assertEquals("wrong count of scheduler events ", 1,
				schedulerEvent.get());

		assure(semWorker,
				1,
				"did not get 1 event (TaskWorker: finished processing) in time",
				DEFAULT_CHECK_TIMEOUT + PROCESSING_TIME / 1000);
		assure(sem, 1, "did not get 1 event (Scheduler: processed) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP3 ------------------------
		logCheckpoint(3, startTime);
		assertEquals("wrong count of taskWorker events", 1, processed.get());
		assertEquals("wrong count of scheduler events ", 2,
				schedulerEvent.get());
		assertEquals("wrong count of taskForce events ", 1,
				taskForceEvent.get());

		log("Executing info " + taskWrapperId1 + "...");
		scheduler.info(taskWrapperId1);

		assure(sem, 1, "did not get 1 event (Scheduler: info) in time",
				DEFAULT_CHECK_TIMEOUT);

		// ---------------- CP4 ------------------------
		logCheckpoint(4, startTime);

		assertEquals("wrong count of scheduler events ", 3,
				schedulerEvent.get());
		assertEquals("wrong count of taskForce events ", 1,
				taskForceEvent.get());
	}

	@After
	public void shutdown() {
		// disable all listeners
		tf1.setTaskForceListener(null);
		scheduler.setSchedulerListener(null);

		log("shutting down tw1...");
		tw1.stop();
		log("shutting down tf1...");
		tf1.stop();
		log("shutting down S...");
		scheduler.stop();

		super.shutdown();
	}

}
