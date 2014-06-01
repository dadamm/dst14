package dst.ass3.jms.taskforce.impl;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.openejb.api.LocalClient;

import dst.ass3.dto.RateTaskWrapperDTO;
import dst.ass3.jms.JmsConstants;
import dst.ass3.jms.taskforce.ITaskForce;
import dst.ass3.model.LifecycleState;

@LocalClient
public class TaskForce implements ITaskForce, MessageListener {
	
	@Resource
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	
	@Resource(name = JmsConstants.SERVER_QUEUE)
	private Queue serverQueue;
	private MessageProducer serverProducer;
	
	@Resource(name = JmsConstants.TASKFORCE_QUEUE)
	private Queue taskforceQueue;
	private MessageConsumer taskforceConsumer;
	
	private String name;
	private ITaskForceListener taskForceListener;
	
	public TaskForce(String name) {
		this.name = name;
	}

	@Override
	public void start() {
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			serverProducer = session.createProducer(serverQueue);
			taskforceConsumer = session.createConsumer(taskforceQueue);
			taskforceConsumer.setMessageListener(this);
			connection.start();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		try {
			if(serverProducer != null) {
				serverProducer.close();
			}
			if(taskforceConsumer != null) {
				taskforceConsumer.close();
			}
			if(session != null) {
				session.close();
			}
			if(connection != null) {
				connection.close();
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setTaskForceListener(ITaskForceListener listener) {
		this.taskForceListener = listener;
	}

	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			connection.stop();
			Object obj = objectMessage.getObject();
			if(obj instanceof RateTaskWrapperDTO) {
				RateTaskWrapperDTO task = (RateTaskWrapperDTO) obj;
				ITaskForceListener.TaskWrapperDecideResponse decide = taskForceListener.decideTask(task, name);
				task.setRatedBy(name);
				if(decide.resp.equals(ITaskForceListener.TaskWrapperResponse.ACCEPT)) {
					task.setComplexity(decide.complexity);
					task.setState(LifecycleState.READY_FOR_PROCESSING);
				} else {
					task.setState(LifecycleState.PROCESSING_NOT_POSSIBLE);
				}
				ObjectMessage response = session.createObjectMessage(task);
				serverProducer.send(response);
				connection.start();
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
