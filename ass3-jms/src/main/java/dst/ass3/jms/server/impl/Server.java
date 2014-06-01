package dst.ass3.jms.server.impl;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst.ass3.dto.InfoTaskWrapperDTO;
import dst.ass3.dto.NewTaskWrapperDTO;
import dst.ass3.dto.ProcessTaskWrapperDTO;
import dst.ass3.dto.RateTaskWrapperDTO;
import dst.ass3.dto.TaskWrapperDTO;
import dst.ass3.jms.JmsConstants;
import dst.ass3.model.ITaskWrapper;
import dst.ass3.model.LifecycleState;
import dst.ass3.model.TaskWrapper;

@MessageDriven(mappedName = JmsConstants.SERVER_QUEUE, activationConfig = {
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
})
public class Server implements MessageListener {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	
	@Resource(name = JmsConstants.SCHEDULER_QUEUE)
	private Queue schedulerQueue;
	private MessageProducer schedulerProducer;
	
	@Resource(name = JmsConstants.TASKFORCE_QUEUE)
	private Queue taskforceQueue;
	private MessageProducer taskforceProducer;
	
	@Resource(name = JmsConstants.TASKWORKER_TOPIC)
	private Topic taskworkerTopic;
	private MessageProducer taskworkerProducer;
	
	@PostConstruct
	private void start() {
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			schedulerProducer = session.createProducer(schedulerQueue);
			taskforceProducer = session.createProducer(taskforceQueue);
			taskworkerProducer = session.createProducer(taskworkerTopic);
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@PreDestroy
	private void stop() {
		try {
			if(schedulerProducer != null) {
				schedulerProducer.close();
			}
			if(taskforceProducer != null) {
				taskforceProducer.close();
			}
			if(taskworkerProducer != null) {
				taskworkerProducer.close();
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
	
	private ITaskWrapper assignTask(NewTaskWrapperDTO newTask) {
		TaskWrapper task = new TaskWrapper(newTask);
		entityManager.persist(task);
		return new TaskWrapperDTO(task);
	}
	
	private ITaskWrapper getTask(InfoTaskWrapperDTO infoTask) {
		TaskWrapper task = entityManager.find(TaskWrapper.class, infoTask.getTaskWrapperId());
		if(task == null) {
			return new TaskWrapperDTO();
		} else {
			return new TaskWrapperDTO(task);
		}
	}
	
	private ITaskWrapper getTask(RateTaskWrapperDTO rateTask) {
		return entityManager.find(TaskWrapper.class, rateTask.getId());
	}
	
	private ITaskWrapper getTask(ProcessTaskWrapperDTO processTask) {
		return entityManager.find(TaskWrapper.class, processTask.getId());
	}

	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			// test scheduler send
			schedulerProducer.send(session.createObjectMessage());
			Object obj = objectMessage.getObject();
			if(obj instanceof InfoTaskWrapperDTO) {
				ObjectMessage response = session.createObjectMessage((Serializable) getTask((InfoTaskWrapperDTO) obj));
				response.setStringProperty(JmsConstants.INFOTYPE_PROPERTY, JmsConstants.INFOTYPE_INFO_PROPERTY);
				schedulerProducer.send(response);
			} else if (obj instanceof NewTaskWrapperDTO) {
				ITaskWrapper task = assignTask((NewTaskWrapperDTO) obj);
				
				ObjectMessage response = session.createObjectMessage((Serializable) task);
				response.setStringProperty(JmsConstants.INFOTYPE_PROPERTY, JmsConstants.INFOTYPE_CREATED_PROPERTY);
				schedulerProducer.send(response);
				
				ObjectMessage forward = session.createObjectMessage(new RateTaskWrapperDTO(task));
				taskforceProducer.send(forward);
			} else if(obj instanceof RateTaskWrapperDTO) {
				RateTaskWrapperDTO rateTask = (RateTaskWrapperDTO) obj;
				ITaskWrapper task = getTask(rateTask);
				task.setRatedBy(rateTask.getRatedBy());
				task.setComplexity(rateTask.getComplexity());
				task.setState(rateTask.getState());
				entityManager.persist(task);
				if(task.getState().equals(LifecycleState.PROCESSING_NOT_POSSIBLE)) {
					ObjectMessage response = session.createObjectMessage(rateTask);
					schedulerProducer.send(response);
				} else if(task.getState().equals(LifecycleState.READY_FOR_PROCESSING)) {
					ProcessTaskWrapperDTO processTask = new ProcessTaskWrapperDTO(task);
					ObjectMessage response = session.createObjectMessage(processTask);
					response.setStringProperty(JmsConstants.TASKFORCE_PROPERTY, processTask.getRatedBy());
					response.setStringProperty(JmsConstants.COMPLEXITY_PROPERTY, processTask.getComplexity().toString());
					taskworkerProducer.send(response);
				}
			} else if(obj instanceof ProcessTaskWrapperDTO) {
				ProcessTaskWrapperDTO processTask = (ProcessTaskWrapperDTO) obj;
				ITaskWrapper task = getTask(processTask);
				if(!task.getState().equals(processTask.getState())) {
					task.setState(processTask.getState());
					entityManager.persist(task);
					ObjectMessage response = session.createObjectMessage(processTask);
					schedulerProducer.send(response);
				}
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
