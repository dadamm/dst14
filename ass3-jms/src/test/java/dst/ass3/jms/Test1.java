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
 * This test performs the following tasks: ASSIGN and DENY.
 * 
 * <pre>
 * Timing diagram                                                                         
 *                                                                                        
 *    0  1  2 [sec]                                                                       
 *    |--|--|-->                                                                          
 * T1 D                                                                                   
 *    ^     ^                                                                             
 *    CP1   CP2                                                                            
 *                                                                                        
 * D: Task denied by TaskForce                                                              
 *                                                                                        
 * T1: task1                                                                              
 *                                                                                        
 * CP1: Check-Point 1 - Assign task1                                                                        
 * CP2: Check-Point 2 - [Scheduler = ASSIGNED, DENNIED] [TaskForce = DENNIED]
 * </pre>
 */
public class Test1 extends AbstractJMSTest {

	private AtomicInteger taskForceEvent = new AtomicInteger(0);
	private AtomicInteger schedulerEvent = new AtomicInteger(0);

	private long taskId = 10;

	private long taskWrapperId = -1;

	private long startTime;

	private Semaphore sem;

	@Before
	public void init() {
		super.init();
	}

	@Test
	public void test_AssignAndDeny() {
		sem = new Semaphore(0);
		tf1.start();
		tf2.start();
		scheduler.start();
		tw1.start();

		ITaskForceListener taskForceListener = new ITaskForceListener() {
			@Override
			public TaskWrapperDecideResponse decideTask(RateTaskWrapperDTO taskWrapper,
					String taskForceName) {
				logTimed("** taskForce " + taskForceName + " taskWrapper: " + taskWrapper,
						startTime);
				taskForceEvent.incrementAndGet();

				assertEquals("only 1 raised event expected", 1,
						taskForceEvent.get());

				assertNotNull("taskWrapper.taskId = null", taskWrapper.getTaskId());
				assertEquals("taskId wrong", taskId, taskWrapper.getTaskId().longValue());

				assertNotNull("taskWrapper.id = null", taskWrapper.getId());

				log("SETTING ID " + taskWrapper.getId());
				taskWrapperId = taskWrapper.getId();
				sem.release();
				return new TaskWrapperDecideResponse(TaskWrapperResponse.DENY, null);
			}
		};

		ISchedulerListener schedulerListener = new ISchedulerListener() {
			@Override
			public void notify(InfoType type, TaskWrapperDTO taskWrapper) {
				logTimed("** scheduler: type=" + type + " taskWrapper: " + taskWrapper,
						startTime);

				sleep(SHORT_WAIT); // wait short time for updated taskId

				assertEquals("taskId in server response DTO wrong "
						+ schedulerEvent, taskId, taskWrapper.getTaskId().longValue());
				assertEquals("taskWrapperId in server response DTO wrong"
						+ schedulerEvent, taskWrapperId, taskWrapper.getId().longValue());

				switch (schedulerEvent.get()) {
				case 0:
					assertEquals("1st event of wrong type", InfoType.CREATED,
							type);
					assertEquals("1st event != ASSIGNED",
							LifecycleState.ASSIGNED, taskWrapper.getState());
					assertEquals("first event complexity != UNRATED",
							Complexity.UNRATED, taskWrapper.getComplexity());
					break;
				case 1:
					assertEquals("2nd event of wrong type", InfoType.DENIED,
							type);
					assertEquals("2nd event != PROCESSING_NOT_POSSIBLE",
							LifecycleState.PROCESSING_NOT_POSSIBLE,
							taskWrapper.getState());
					assertEquals("2nd event complexity != UNRATED",
							Complexity.UNRATED, taskWrapper.getComplexity());
					break;
				default:
					fail("only 2 events expected");
					break;
				}
				schedulerEvent.incrementAndGet();
				sem.release();
			}
		};

		sleep(SHORT_WAIT); // Wait for old messages coming in.
		startTime = new Date().getTime();

		// ---------------- CP1 ------------------------
		logCheckpoint(1, startTime);

		tf1.setTaskForceListener(taskForceListener);
		tf2.setTaskForceListener(taskForceListener);

		scheduler.setSchedulerListener(schedulerListener);

		log("Assigning " + taskId + "...");
		scheduler.assign(taskId);

		// ---------------- CP2 ------------------------
		logCheckpoint(2, startTime);
		assure(sem,
				3,
				"did not get 3 events (Scheduler: create, denied; TaskForce: rate) in time",
				DEFAULT_CHECK_TIMEOUT);
		assertEquals("wrong count of scheduler events ", 2,
				schedulerEvent.get());
		assertEquals("wrong count of taskForce events ", 1, taskForceEvent.get());
	}

	@After
	public void shutdown() {
		// disable all listeners
		tf1.setTaskForceListener(null);
		tf2.setTaskForceListener(null);
		scheduler.setSchedulerListener(null);

		log("shutting down tw1...");
		tw1.stop();
		log("shutting down tf1...");
		tf1.stop();
		log("shutting down tf2...");
		tf2.stop();
		log("shutting down S...");
		scheduler.stop();
		
		super.shutdown();
	}
}
