package dst.ass3.event;

import static dst.ass3.EventingUtils.ESPER_CHECK_TIMEOUT;
import static dst.ass3.util.Utils.SHORT_WAIT;
import static dst.ass3.util.Utils.assure;
import static dst.ass3.util.Utils.logCheckpoint;
import static dst.ass3.util.Utils.logTimed;
import static dst.ass3.util.Utils.sleep;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.junit.Test;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

import dst.ass3.AbstractEventTest;
import dst.ass3.EventingUtils;
import dst.ass3.dto.TaskWrapperDTO;
import dst.ass3.model.Complexity;
import dst.ass3.model.ITaskWrapper;
import dst.ass3.model.LifecycleState;

/**
 * Use pattern matching facilities of EPL to detect tasks which have 3 times
 * attempted and failed to execute (i.e., switched 3 times between the status
 * READY FOR PROCESSING and the status PROCESSING NOT POSSIBLE).
 */
public class Test6 extends AbstractEventTest {

	private Semaphore semSwitch;

	@Test
	public void test_PatternMatchingQuery() {
		final long startTime = System.currentTimeMillis();

		semSwitch = new Semaphore(0);
		test = EventingFactory.getInstance();

		ITaskWrapper t1 = EventingFactory.createTaskWrapper(601L, 611L,
				LifecycleState.ASSIGNED, "tf1", Complexity.EASY);

		test.initializeAll(new StatementAwareUpdateListener() {

			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents,
					EPStatement s, EPServiceProvider p) {
				try {
					for (EventBean e : newEvents) {
						String name = e.getEventType().getName();
						if (name.equals(Constants.EVENT_TASK_ASSIGNED)
								|| name.equals(Constants.EVENT_TASK_PROCESSED)
								|| name.equals(Constants.EVENT_TASK_DURATION)) {
							return;
						}

						List<Object> eventBeans = new LinkedList<Object>();
						eventBeans.addAll(EventingUtils.getAliasedEventBeans(e));
						if(eventBeans.isEmpty()) {
							eventBeans.addAll(EventingUtils.getAliasedEventObjects(e, Object.class));
						}

						for (Object o : eventBeans) {
							if (o != null) {
								LifecycleState state = null;
								Complexity complexity = null;
								String ratedBy = null;
								Long taskID = null;

								if(o instanceof EventBean) {
									EventBean task = (EventBean)o;
									state = getTaskStatus(task);
									if (state == null
											|| !state
													.equals(LifecycleState.READY_FOR_PROCESSING))
										continue;
									complexity = getTaskComplexity(task);
									ratedBy = task.get("ratedBy").toString();
									taskID = EventingUtils.getLong(task, "taskId").longValue();
									System.out.println("task id "
											+ task.getUnderlying());
								} else {
									TaskWrapperDTO[] dtos = (TaskWrapperDTO[])o;
									TaskWrapperDTO dto = dtos[0];
									state = dto.getState();
									complexity = dto.getComplexity();
									ratedBy = dto.getRatedBy();
									taskID = dto.getTaskId();
								}

								assertEquals("complexity wrong",
										Complexity.EASY, complexity);
								assertEquals("ratedBy wrong", "tf1", ratedBy);
								assertEquals("taskId wrong", 611L, (long)taskID);
								semSwitch.release();

								break;
							}
						}
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}, false);

		sleep(SHORT_WAIT); // wait for setup
		logCheckpoint(0, startTime);

		t1.setState(LifecycleState.READY_FOR_PROCESSING);
		test.addEvent(t1);

		for (int i = 0; i < 3; i++) {
			t1.setState(LifecycleState.PROCESSING_NOT_POSSIBLE);
			test.addEvent(t1);
			sleep(SHORT_WAIT);

			t1.setState(LifecycleState.READY_FOR_PROCESSING);
			test.addEvent(t1);
		}

		sleep(SHORT_WAIT); // wait for all events
		logTimed("checking results", startTime);

		assure(semSwitch, 1, "1 switch event expected!", ESPER_CHECK_TIMEOUT);
	}
}
