package dst.ass3.event;

import static dst.ass3.EventingUtils.ESPER_CHECK_TIMEOUT;
import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.logCheckpoint;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;
import static org.junit.Assert.fail;

import java.util.concurrent.Semaphore;

import org.junit.Test;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

import dst.ass3.AbstractEventTest;
import dst.ass3.EventingUtils;
import dst.ass3.model.Complexity;
import dst.ass3.model.ITaskWrapper;
import dst.ass3.model.LifecycleState;

/**
 * Checks TaskDuration query.
 * 
 * Timing:
 * 
 * <pre>
 *  2s   2s  2s  2s
 * |---|---|---|---|---|---|
 * ^   ^   ^       ^
 * t1  t3  t1      t2
 * t2
 * t3
 * t4
 * </pre>
 */
public class Test4 extends AbstractEventTest {

	private Semaphore semDuration;

	private long[] taskWrapperIds = { 401L, 402L, 403L, 404L };
	private long[] taskIds = { 411, 412, 413, 414 };
	private final String RATED_BY = "tf1";

	@Test
	public void test_TaskDurationQuery() {

		semDuration = new Semaphore(0);

		ITaskWrapper t0 = EventingFactory.createTaskWrapper(taskWrapperIds[0],
				taskIds[0], LifecycleState.ASSIGNED, RATED_BY, Complexity.EASY);
		ITaskWrapper t1 = EventingFactory.createTaskWrapper(taskWrapperIds[1],
				taskIds[1], LifecycleState.ASSIGNED, RATED_BY, Complexity.EASY);
		ITaskWrapper t2 = EventingFactory.createTaskWrapper(taskWrapperIds[2],
				taskIds[2], LifecycleState.ASSIGNED, RATED_BY, Complexity.EASY);
		ITaskWrapper t3 = EventingFactory.createTaskWrapper(taskWrapperIds[3],
				taskIds[3], LifecycleState.ASSIGNED, RATED_BY, Complexity.EASY);

		test.initializeAll(new StatementAwareUpdateListener() {

			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents,
					EPStatement s, EPServiceProvider p) {
				Long duration = null;
				for (EventBean e : newEvents) {
					System.out.println("LISTENER:" + e.getEventType().getName()
							+ " " + e.getUnderlying());
					String name = e.getEventType().getName();
					if (name.equals(Constants.EVENT_TASK_DURATION)) {
						EventingUtils.ensureTaskId("Duration", e, taskIds);
						duration = EventingUtils.getLong(e, "duration");
						long taskId = EventingUtils.getLong(e, "taskId");

						if (taskId == taskIds[0]) {
							EventingUtils.ensureRange("Duration " + taskId,
									4000, duration, allowedInaccuracy);
							semDuration.release();
						} else if (taskId == taskIds[1]) {
							EventingUtils.ensureRange("Duration " + taskId,
									8000, duration, allowedInaccuracy);
							semDuration.release();
						} else if (taskId == taskIds[2]) {
							EventingUtils.ensureRange("Duration " + taskId,
									2000, duration, allowedInaccuracy);
							semDuration.release();
						} else {
							fail("unknown taskId! Not expected " + taskId);
						}

					}
				}

			}
		}, false);

		sleep(SHORT_WAIT); // wait for setup
		final long startTime = System.currentTimeMillis();
		logCheckpoint(0, startTime);

		test.addEvent(t0);
		test.addEvent(t1);
		test.addEvent(t2);
		test.addEvent(t3);

		sleep(2000); // fixed 2sec see description
		logCheckpoint(1, startTime);

		t2.setState(LifecycleState.PROCESSED);
		test.addEvent(t2);

		sleep(2000); // fixed 2sec see description
		logCheckpoint(2, startTime);

		t0.setState(LifecycleState.PROCESSED);
		test.addEvent(t0);

		sleep(2000); // fixed 2sec see description
		sleep(2000); // fixed 2sec see description
		logCheckpoint(3, startTime);

		t1.setState(LifecycleState.PROCESSED);
		test.addEvent(t1);

		// t4 never reaches processed

		logTimed("checking results", startTime);
		assure(semDuration, 3, "3 taskDuration event expected",
				ESPER_CHECK_TIMEOUT);

	}
}
