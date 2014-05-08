package dst.ass3;

import org.junit.After;
import org.junit.Before;

import com.espertech.esper.client.EventBean;

import dst.ass3.event.EventingFactory;
import dst.ass3.event.IEventProcessing;
import dst.ass3.model.Complexity;
import dst.ass3.model.LifecycleState;

public abstract class AbstractEventTest {

	protected final int allowedInaccuracy = 500;

	protected IEventProcessing test;

	@Before
	public void setup() {
		System.out.println("******************************"
				+ this.getClass().getCanonicalName()
				+ "******************************");

		test = EventingFactory.getInstance();
	}

	@After
	public void shutdown() {
		test.close();
	}

	protected Complexity getTaskComplexity(EventBean e) {
		try {
			return (Complexity) e.get("complexity");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	protected LifecycleState getTaskStatus(EventBean e) {
		try {
			return (LifecycleState) e.get("state");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
