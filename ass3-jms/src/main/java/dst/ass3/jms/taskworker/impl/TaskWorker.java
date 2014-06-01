package dst.ass3.jms.taskworker.impl;

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
import javax.jms.Topic;

import org.apache.openejb.api.LocalClient;

import dst.ass3.dto.ProcessTaskWrapperDTO;
import dst.ass3.jms.JmsConstants;
import dst.ass3.jms.taskworker.ITaskWorker;
import dst.ass3.model.Complexity;
import dst.ass3.model.LifecycleState;

@LocalClient
public class TaskWorker implements ITaskWorker, MessageListener {
	
	@Resource
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	
	@Resource(name = JmsConstants.TASKWORKER_TOPIC)
	private Topic taskworkerTopic;
	private MessageConsumer taskworkerConsumer;
	
	@Resource(name = JmsConstants.SERVER_QUEUE)
	private Queue serverQueue;
	private MessageProducer serverProducer;
	
	private ITaskWorkerListener taskWorkerListener;
	
	private String name;
	private String taskForce;
	private Complexity complexity;
	private String clientId;
	
	public TaskWorker(String name, String taskForce, Complexity complexity) {
		this.name = name;
		this.taskForce = taskForce;
		this.complexity = complexity;
		this.clientId = name + "_" + taskForce;
	}
	
	private String getMessageSelectorString() {
		StringBuilder sb = new StringBuilder();
		sb.append(JmsConstants.TASKFORCE_PROPERTY + "='" + taskForce + "'");
		sb.append(" and ");
		sb.append(JmsConstants.COMPLEXITY_PROPERTY + "='" + complexity.toString() + "'"); 
		return sb.toString();
	}

	@Override
	public void start() {
		try {
			connection = connectionFactory.createConnection();
			connection.setClientID(clientId);
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			serverProducer = session.createProducer(serverQueue);

			taskworkerConsumer = session.createDurableSubscriber(taskworkerTopic, "taskworkertopic", getMessageSelectorString(), false);
			taskworkerConsumer.setMessageListener(this);
			
			connection.start();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void stop() {
		try {
			if(serverProducer != null) {
				serverProducer.close();
			}
			if(taskworkerConsumer != null) {
				taskworkerConsumer.close();
			}
			if(session != null) {
				session.close();
			}
			if(connection != null) {
				connection.close();
			}
		} catch (JMSException e) {
			System.out.println("!!taskworker closing connection error!!");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setTaskWorkerListener(ITaskWorkerListener listener) {
		this.taskWorkerListener = listener;
	}

	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			Object obj = objectMessage.getObject();
			if(obj instanceof ProcessTaskWrapperDTO) {
				ProcessTaskWrapperDTO processTask = (ProcessTaskWrapperDTO) obj;
				taskWorkerListener.waitTillProcessed(processTask, name, complexity, taskForce);
				processTask.setState(LifecycleState.PROCESSED);
				
				ObjectMessage response = session.createObjectMessage(processTask);
				serverProducer.send(response);
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
