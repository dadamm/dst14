package dst.ass3;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;

import dst.ass3.jms.JMSFactory;
import dst.ass3.jms.scheduler.IScheduler;
import dst.ass3.jms.taskforce.ITaskForce;
import dst.ass3.jms.taskworker.ITaskWorker;
import dst.ass3.model.Complexity;

public abstract class AbstractJMSTest {

	protected static final String TASKWORKER1_NAME = "tw1";
	protected static final String TASKWORKER2_NAME = "tw2";
	protected static final String TASKWORKER3_NAME = "tw3";
	protected static final String TASKWORKER4_NAME = "tw4";

	protected static final String TASKFORCE1_NAME = "tf1";
	protected static final String TASKFORCE2_NAME = "tf2";

	protected IScheduler scheduler;
	protected ITaskForce tf2;
	protected ITaskForce tf1;
	protected ITaskWorker tw4;
	protected ITaskWorker tw3;
	protected ITaskWorker tw2;
	protected ITaskWorker tw1;

	protected EJBContainer container;

	/**
	 * Time to wait for semaphores to reach required value
	 */
	public int DEFAULT_CHECK_TIMEOUT = 5;

	public void init() {
		this.tw1 = JMSFactory.createTaskWorker(TASKWORKER1_NAME,
				TASKFORCE1_NAME, Complexity.CHALLENGING);
		this.tw2 = JMSFactory.createTaskWorker(TASKWORKER2_NAME,
				TASKFORCE1_NAME, Complexity.EASY);
		this.tw3 = JMSFactory.createTaskWorker(TASKWORKER3_NAME,
				TASKFORCE2_NAME, Complexity.CHALLENGING);
		this.tw4 = JMSFactory.createTaskWorker(TASKWORKER4_NAME,
				TASKFORCE2_NAME, Complexity.EASY);

		this.tf1 = JMSFactory.createTaskForce(TASKFORCE1_NAME);
		this.tf2 = JMSFactory.createTaskForce(TASKFORCE2_NAME);

		this.scheduler = JMSFactory.createScheduler();

		System.setProperty("openejb.validation.output.level", "VERBOSE");
		container = EJBContainer.createEJBContainer();

		try {
			Properties p = new Properties();
			p.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.apache.openejb.client.LocalInitialContextFactory");
			InitialContext ctx = new InitialContext(p);

			ctx.bind("inject", scheduler);

			ctx.bind("inject", tf1);
			ctx.bind("inject", tf2);

			ctx.bind("inject", tw1);
			ctx.bind("inject", tw2);
			ctx.bind("inject", tw3);
			ctx.bind("inject", tw4);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		System.out.println("******************************"
				+ this.getClass().getCanonicalName()
				+ "******************************");
	}

	public void shutdown() {
		if (container != null)
			container.close();
	}

}
