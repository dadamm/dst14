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
import dst.ass3.dto.RateTaskWrapperDTO;
import dst.ass3.dto.TaskWrapperDTO;
import dst.ass3.jms.scheduler.IScheduler.ISchedulerListener;
import dst.ass3.jms.taskforce.ITaskForce.ITaskForceListener;
import dst.ass3.model.Complexity;
import dst.ass3.model.LifecycleState;

/**
 * This test performs the following tasks: ASSIGN, RATE as EASY and perform INFO
 * 2 times.
 * 
 * <pre>
 * Timing diagram
 * 
 *    0  1  2  3  4 [sec]                                                                 
 *    |--|--|--|--|-->                                                                    
 * T1 ###################...                                                              
 * I1       *                                                                             
 * I2       *                                                                             
 *    ^     ^     ^                                                                       
 *    CP1   CP2   CP3                                                                      
 *                                                                                        
 * ##: waiting for processing (no computer listener = computer discards message)
 * 
 * T1: task1
 * I1,I2: Info Request 1,2
 * CP1: Check-Point 1 - assign task1, TaskForce accepts as EASY                                               
 * CP2: Check-Point 2 - request info 2x [Scheduler = ASSIGN]                                               
 * CP3: Check-Point 3 - info finished  [Scheduler = INFO, INFO]
 * </pre>
 */
public class Test3 extends AbstractJMSTest {

	private AtomicInteger taskForceEvent = new AtomicInteger(0);
	private AtomicInteger schedulerEvent = new AtomicInteger(0);

	private long taskId1 = 30;
	private long taskWrapperId1 = -1;

	private String ratedBy = null;
	private long startTime;

	private Semaphore sem;

	@Before
	public void init() {
		super.init();
	}

	@Test
	public void test_AssignRateAndInfo() {
		sem = new Semaphore(0);

		tf1.start();
		tf2.start();
		scheduler.start();
		tw2.start();
		tw4.start();

		ITaskForceListener taskForceListener1 = new ITaskForceListener() {
			@Override
			public TaskWrapperDecideResponse decideTask(
					RateTaskWrapperDTO taskWrapper, String taskForceName) {
				logTimed("** taskForce " + taskForceName + " taskWrapper: " + taskWrapper,
						startTime);

				taskForceEvent.incrementAndGet();

				assertNotNull("taskWrapper.taskId = null", taskWrapper.getTaskId());
				assertNotNull("taskWrapper.id = null", taskWrapper.getId());

				assertEquals("taskId wrong", taskId1, taskWrapper.getTaskId()
						.longValue());

				log("SETTING ID " + taskWrapper.getId());
				taskWrapperId1 = taskWrapper.getId();

				ratedBy = TASKFORCE1_NAME;
				assertEquals("reported taskForce wrong", ratedBy, taskForceName);

				sem.release();
				return new TaskWrapperDecideResponse(
						TaskWrapperResponse.ACCEPT, Complexity.EASY);
			}
		};

		ITaskForceListener taskForceListener2 = new ITaskForceListener() {
			@Override
			public TaskWrapperDecideResponse decideTask(
					RateTaskWrapperDTO taskWrapper, String taskForceName) {
				logTimed("** taskForce " + taskForceName + " taskWrapper: " + taskWrapper,
						startTime);

				taskForceEvent.incrementAndGet();

				assertNotNull("taskWrapper.taskId = null", taskWrapper.getTaskId());
				assertNotNull("taskWrapper.id = null", taskWrapper.getId());

				assertEquals("taskId wrong", taskId1, taskWrapper.getTaskId()
						.longValue());

				log("SETTING ID " + taskWrapper.getId());
				taskWrapperId1 = taskWrapper.getId();

				ratedBy = TASKFORCE2_NAME;
				assertEquals("reported taskForce wrong", ratedBy, taskForceName);

				sem.release();
				return new TaskWrapperDecideResponse(
						TaskWrapperResponse.ACCEPT, Complexity.EASY);
			}
		};

		ISchedulerListener schedulerListener = new ISchedulerListener() {
			@Override
			public void notify(InfoType type, TaskWrapperDTO taskWrapper) {
				logTimed("** scheduler: type=" + type + " taskWrapper: " + taskWrapper,
						startTime);

				sleep(SHORT_WAIT); // wait short time for updated taskId

				assertEquals("taskId in server response DTO wrong " + taskId1,
						taskId1, taskWrapper.getTaskId().longValue());
				assertEquals("taskWrapperId in server response DTO wrong"
						+ schedulerEvent, taskWrapperId1, taskWrapper.getId().longValue());

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
					// INFO 1
					assertEquals("2nd event of wrong type", InfoType.INFO, type);
					assertEquals("2nd event != ASSIGNED",
							LifecycleState.READY_FOR_PROCESSING,
							taskWrapper.getState());
					assertEquals("2nd event complexity != EASY",
							Complexity.EASY, taskWrapper.getComplexity());
					assertNotNull("2nd rated by == null" + ratedBy,
							taskWrapper.getRatedBy());
					assertEquals("2nd rated by != " + ratedBy, ratedBy,
							taskWrapper.getRatedBy());
					break;
				case 2:
					// INFO 2
					assertEquals("3rd event of wrong type", InfoType.INFO, type);
					assertEquals("3rd event != ASSIGNED",
							LifecycleState.READY_FOR_PROCESSING,
							taskWrapper.getState());
					assertEquals("3rd event complexity != EASY",
							Complexity.EASY, taskWrapper.getComplexity());
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

		tf1.setTaskForceListener(taskForceListener1);
		tf2.setTaskForceListener(taskForceListener2);
		scheduler.setSchedulerListener(schedulerListener);

		log("Assigning " + taskId1 + "...");
		scheduler.assign(taskId1);

		// ---------------- CP2 ------------------------
		logCheckpoint(2, startTime);
		assure(sem,
				2,
				"did not get 2 events (Scheduler: assign, TaskForce: rate) in time",
				DEFAULT_CHECK_TIMEOUT);

		assertEquals("wrong count of taskForce events ", 1, taskForceEvent.get());
		assertEquals("wrong count of scheduler events ", 1,
				schedulerEvent.get());

		log("Executing info 2x " + taskWrapperId1 + "...");
		scheduler.info(taskWrapperId1);
		scheduler.info(taskWrapperId1);

		// ---------------- CP3 ------------------------
		logCheckpoint(3, startTime);
		assure(sem, 2, "did not get 2 events (Scheduler: info, info) in time",
				DEFAULT_CHECK_TIMEOUT);

		assertEquals("wrong count of scheduler events ", 3,
				schedulerEvent.get());
		assertEquals("wrong count of taskForce events ", 1, taskForceEvent.get());
	}

	@After
	public void shutdown() {
		// disable all listeners
		tf1.setTaskForceListener(null);
		tf2.setTaskForceListener(null);
		scheduler.setSchedulerListener(null);

		log("shutting down tf1...");
		tf1.stop();
		log("shutting down tf2...");
		tf2.stop();
		log("shutting down tw2,4...");
		tw2.stop();
		tw4.stop();
		log("shutting down S...");
		scheduler.stop();
		
		super.shutdown();
	}

}
