package dst.ass3.jms.scheduler.impl;

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

import dst.ass3.dto.InfoTaskWrapperDTO;
import dst.ass3.dto.NewTaskWrapperDTO;
import dst.ass3.dto.TaskWrapperDTO;
import dst.ass3.jms.JmsConstants;
import dst.ass3.jms.scheduler.IScheduler;
import dst.ass3.jms.scheduler.IScheduler.ISchedulerListener.InfoType;

@LocalClient
public class Scheduler implements IScheduler, MessageListener {
	
	@Resource
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	
	@Resource(name = JmsConstants.SCHEDULER_QUEUE)
	private Queue schedulerQueue;
	private MessageConsumer schedulerConsumer;
	
	@Resource(name = JmsConstants.SERVER_QUEUE)
	private Queue serverQueue;
	private MessageProducer serverProducer;
	
	private ISchedulerListener schedulerListener;
	
	@Override
	public void start() {
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			serverProducer = session.createProducer(serverQueue);
			schedulerConsumer = session.createConsumer(schedulerQueue);
			schedulerConsumer.setMessageListener(this);
			
			connection.start();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void stop() {
		try {
			if(schedulerConsumer != null) {
				schedulerConsumer.close();
			}
			if(serverProducer != null) {
				serverProducer.close();
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
	public void assign(long taskId) {
		NewTaskWrapperDTO task = new NewTaskWrapperDTO(taskId);
		try {
			Message objectMessage = session.createObjectMessage(task);
			serverProducer.send(objectMessage);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void info(long taskWrapperId) {
		InfoTaskWrapperDTO task = new InfoTaskWrapperDTO(taskWrapperId);
		try {
			Message objectMessage = session.createObjectMessage(task);
			serverProducer.send(objectMessage);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void setSchedulerListener(ISchedulerListener listener) {
		this.schedulerListener = listener;
	}

	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			Object obj = objectMessage.getObject();
			if(obj instanceof TaskWrapperDTO) {
				if(objectMessage.getStringProperty(JmsConstants.INFOTYPE_PROPERTY).equals(JmsConstants.INFOTYPE_CREATED_PROPERTY)) {
					schedulerListener.notify(InfoType.CREATED, (TaskWrapperDTO) obj);
				} else if(objectMessage.getStringProperty(JmsConstants.INFOTYPE_PROPERTY).equals(JmsConstants.INFOTYPE_INFO_PROPERTY)) {
					schedulerListener.notify(InfoType.INFO, (TaskWrapperDTO) obj);
				} else if(objectMessage.getStringProperty(JmsConstants.INFOTYPE_PROPERTY).equals(JmsConstants.INFOTYPE_DENIED_PROPERTY)) {
					schedulerListener.notify(InfoType.DENIED, (TaskWrapperDTO) obj);
				} else if(objectMessage.getStringProperty(JmsConstants.INFOTYPE_PROPERTY).equals(JmsConstants.INFOTYPE_PROCESSED_PROPERTY)) {
					schedulerListener.notify(InfoType.PROCESSED, (TaskWrapperDTO) obj);
				}
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
