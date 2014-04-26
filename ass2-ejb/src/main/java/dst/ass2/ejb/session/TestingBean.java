package dst.ass2.ejb.session;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst.ass1.jpa.model.IAddress;
import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.IPerson;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.model.ModelFactory;
import dst.ass1.jpa.model.TaskStatus;
import dst.ass2.ejb.session.interfaces.ITestingBean;

@Stateless
@Remote(ITestingBean.class)
public class TestingBean implements ITestingBean {
	
	@PersistenceContext	private EntityManager em;

	@Override
	public void insertTestData() {

		ModelFactory modelFactory = new ModelFactory();

		System.out.println("Started");

		IAddress address1 = modelFactory.createAddress();
		IAddress address2 = modelFactory.createAddress();
		IAddress address3 = modelFactory.createAddress();

		address1.setCity("city1");
		address1.setStreet("street1");
		address1.setZipCode("1140");

		address2.setCity("city2");
		address2.setStreet("street2");
		address2.setZipCode("1150");

		address3.setCity("city3");
		address3.setStreet("street3");
		address3.setZipCode("1160");

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		IUser user1 = modelFactory.createUser();
		user1.setAccountNo("111111");
		user1.setBankCode("1111");
		((IPerson) user1).setFirstName("Hans");
		((IPerson) user1).setLastName("Mueller");
		((IPerson) user1).setAddress(address1);
		user1.setUsername("hansi");
		user1.setPassword(md.digest("pw".getBytes()));

		IUser user2 = modelFactory.createUser();
		user2.setAccountNo("222222");
		user2.setBankCode("1111");
		((IPerson) user2).setFirstName("Franz");
		((IPerson) user2).setLastName("Mueller");
		((IPerson) user2).setAddress(address2);
		user2.setUsername("franz");
		user2.setPassword(md.digest("liebe".getBytes()));

		IExpert expert = modelFactory.createExpert();
		((IPerson) expert).setFirstName("Sepp");
		((IPerson) expert).setLastName("Huber");
		((IPerson) expert).setAddress(address3);

		em.persist(user1);
		em.persist(user2);
		em.persist(expert);

		IWorkPlatform platform1 = modelFactory.createPlatform();
		platform1.setName("platform1");
		platform1.setLocation("location1");
		platform1.setCostsPerWorkUnit(new BigDecimal(5));

		IWorkPlatform platform2 = modelFactory.createPlatform();
		platform2.setName("platform2");
		platform2.setLocation("location2");
		platform2.setCostsPerWorkUnit(new BigDecimal(7));

		em.persist(platform1);
		em.persist(platform2);

		ITaskForce tf1 = modelFactory.createTaskForce();
		tf1.setName("cl1");
		tf1.setLastMeeting(new Date());
		tf1.setNextMeeting(new Date());
		tf1.setExpert(expert);
		tf1.setWorkPlatform(platform1);

		ITaskForce tf2 = modelFactory.createTaskForce();
		tf2.setName("cl2");
		tf2.setLastMeeting(new Date());
		tf2.setNextMeeting(new Date());
		tf2.setExpert(expert);
		tf2.setWorkPlatform(platform2);

		expert.addAdvisedTaskForce(tf1);
		expert.addAdvisedTaskForce(tf2);

		ITaskWorker worker1 = modelFactory.createTaskWorker();
		worker1.setName("longername1");
		worker1.setWorkUnitCapacity(new Integer(4));
		worker1.setLocation("AUT-XYZ@5678");
		worker1.setJoinedDate(createDate(2000, 10, 10));
		worker1.setLastTraining(createDate(2000, 10, 11));
		worker1.setTaskForce(tf1);

		ITaskWorker worker2 = modelFactory.createTaskWorker();
		worker2.setName("longername2");
		worker2.setWorkUnitCapacity(new Integer(6));
		worker2.setLocation("AUT-XYZ@1234");
		worker2.setJoinedDate(createDate(2000, 10, 12));
		worker2.setLastTraining(createDate(2000, 10, 13));
		worker2.setTaskForce(tf1);

		ITaskWorker worker3 = modelFactory.createTaskWorker();
		worker3.setName("longername3");
		worker3.setWorkUnitCapacity(new Integer(8));
		worker3.setLocation("AUT-XYZ@1234");
		worker3.setJoinedDate(createDate(2000, 10, 14));
		worker3.setLastTraining(createDate(2000, 10, 15));
		worker3.setTaskForce(tf1);

		tf1.addTaskWorker(worker1);
		tf1.addTaskWorker(worker2);
		tf1.addTaskWorker(worker3);

		ITaskWorker worker4 = modelFactory.createTaskWorker();
		worker4.setName("longername4");
		worker4.setWorkUnitCapacity(new Integer(4));
		worker4.setLocation("AUT-XYZ@5678");
		worker4.setJoinedDate(createDate(2000, 10, 16));
		worker4.setLastTraining(createDate(2000, 10, 17));
		worker4.setTaskForce(tf2);

		ITaskWorker worker5 = modelFactory.createTaskWorker();
		worker5.setName("longername5");
		worker5.setWorkUnitCapacity(new Integer(8));
		worker5.setLocation("AUT-XYZ@5678");
		worker5.setJoinedDate(createDate(2000, 10, 18));
		worker5.setLastTraining(createDate(2000, 10, 19));
		worker5.setTaskForce(tf2);

		tf2.addTaskWorker(worker4);
		tf2.addTaskWorker(worker5);

		em.persist(tf1);
		em.persist(tf2);

		em.persist(worker1);
		em.persist(worker2);
		em.persist(worker3);
		em.persist(worker4);
		em.persist(worker5);
		em.persist(expert);
		em.persist(tf2);
		em.persist(platform1);
		em.persist(platform2);

		IMembership membership1 = modelFactory.createMembership();
		IMembershipKey keyId1 = modelFactory.createMembershipKey();
		keyId1.setWorkPlatform(platform1);
		keyId1.setUser(user1);
		membership1.setRegistration(createDate(2009, 1, 1));
		membership1.setDiscount(new Double(0.1));
		membership1.setId(keyId1);

		IMembership membership2 = modelFactory.createMembership();
		IMembershipKey keyId2 = modelFactory.createMembershipKey();
		keyId2.setWorkPlatform(platform1);
		keyId2.setUser(user2);
		membership2.setRegistration(createDate(2008, 2, 2));
		membership2.setDiscount(new Double(0.2));
		membership2.setId(keyId2);

		IMembership membership3 = modelFactory.createMembership();
		IMembershipKey keyId3 = modelFactory.createMembershipKey();
		keyId3.setWorkPlatform(platform2);
		keyId3.setUser(user1);
		membership3.setRegistration(createDate(2007, 3, 3));
		membership3.setDiscount(new Double(0.3));
		membership3.setId(keyId3);

		em.persist(membership1);
		em.persist(membership2);
		em.persist(membership3);

		IMetadata env1 = modelFactory.createMetadata();
		env1.setContext("workflow1");
		env1.setSettings(new ArrayList<String>());

		IMetadata env2 = modelFactory.createMetadata();
		env2.setContext("workflow2");
		env2.setSettings(new ArrayList<String>());

		em.persist(env1);
		em.persist(env2);

		ITaskProcessing ex1 = modelFactory.createTaskProcessing();
		ex1.setStart(new Date(System.currentTimeMillis() - 1800000));
		ex1.setStatus(TaskStatus.SCHEDULED);

		ITask task1 = modelFactory.createTask();
		task1.setAssignedWorkUnits(new Integer(3));
		task1.setProcessingTime(new Integer(0));
		task1.setMetadata(env1);
		task1.setUser(user1);
		task1.setTaskProcessing(ex1);

		ex1.setTask(task1);
		ex1.addWorker(worker3);
		worker3.addTaskProcessing(ex1);
		em.persist(task1);
		em.persist(worker3);

		System.out.println("Finished");

	}

	private Date createDate(int year, int month, int day) {

		String temp = year + "/" + month + "/" + day;
		Date date = null;

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			date = formatter.parse(temp);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;

	}

}
