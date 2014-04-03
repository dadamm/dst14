package dst.ass2.ejb.session;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.EntityManager;

import dst.ass1.jpa.model.IAddress;
import dst.ass1.jpa.model.IExpert;
import dst.ass1.jpa.model.ITaskForce;
import dst.ass1.jpa.model.ITaskWorker;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.IPerson;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.TaskStatus;
import dst.ass1.jpa.model.ModelFactory;
import dst.ass1.jpa.util.test.TestData;
import dst.ass2.ejb.session.interfaces.ITestingBean;

public class TestingBean implements ITestingBean {

	private EntityManager em;

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

		IWorkPlatform ent1_1 = modelFactory.createPlatform();
		ent1_1.setName(TestData.N_ENT1_1);
		ent1_1.setLocation("location1");
		ent1_1.setCostsPerWorkUnit(new BigDecimal(5));

		IWorkPlatform ent1_2 = modelFactory.createPlatform();
		ent1_2.setName(TestData.N_ENT1_2);
		ent1_2.setLocation("location2");
		ent1_2.setCostsPerWorkUnit(new BigDecimal(7));

		em.persist(ent1_1);
		em.persist(ent1_2);

		ITaskForce ent2_1 = modelFactory.createTaskForce();
		ent2_1.setName("tf1");
		ent2_1.setLastMeeting(new Date());
		ent2_1.setNextMeeting(new Date());
		ent2_1.setExpert(expert);
		ent2_1.setWorkPlatform(ent1_1);

		ITaskForce ent2_2 = modelFactory.createTaskForce();
		ent2_2.setName("tf2");
		ent2_2.setLastMeeting(new Date());
		ent2_2.setNextMeeting(new Date());
		ent2_2.setExpert(expert);
		ent2_2.setWorkPlatform(ent1_2);

		expert.addAdvisedTaskForce(ent2_1);
		expert.addAdvisedTaskForce(ent2_2);

		ITaskWorker ent3_1 = modelFactory.createTaskWorker();
		ent3_1.setName("longername1");
		ent3_1.setWorkUnitCapacity(new Integer(4));
		ent3_1.setLocation("AUT-XYZ@5678");
		ent3_1.setJoinedDate(createDate(2000, 10, 10));
		ent3_1.setLastTraining(createDate(2000, 10, 11));
		ent3_1.setTaskForce(ent2_1);

		ITaskWorker ent3_2 = modelFactory.createTaskWorker();
		ent3_2.setName("longername2");
		ent3_2.setWorkUnitCapacity(new Integer(6));
		ent3_2.setLocation("AUT-XYZ@1234");
		ent3_2.setJoinedDate(createDate(2000, 10, 12));
		ent3_2.setLastTraining(createDate(2000, 10, 13));
		ent3_2.setTaskForce(ent2_1);

		ITaskWorker ent3_3 = modelFactory.createTaskWorker();
		ent3_3.setName("longername3");
		ent3_3.setWorkUnitCapacity(new Integer(8));
		ent3_3.setLocation("AUT-XYZ@1234");
		ent3_3.setJoinedDate(createDate(2000, 10, 14));
		ent3_3.setLastTraining(createDate(2000, 10, 15));
		ent3_3.setTaskForce(ent2_1);

		ent2_1.addTaskWorker(ent3_1);
		ent2_1.addTaskWorker(ent3_2);
		ent2_1.addTaskWorker(ent3_3);

		ITaskWorker ent3_4 = modelFactory.createTaskWorker();
		ent3_4.setName("longername4");
		ent3_4.setWorkUnitCapacity(new Integer(4));
		ent3_4.setLocation("AUT-XYZ@5678");
		ent3_4.setJoinedDate(createDate(2000, 10, 16));
		ent3_4.setLastTraining(createDate(2000, 10, 17));
		ent3_4.setTaskForce(ent2_2);

		ITaskWorker ent3_5 = modelFactory.createTaskWorker();
		ent3_5.setName("longername5");
		ent3_5.setWorkUnitCapacity(new Integer(8));
		ent3_5.setLocation("AUT-XYZ@5678");
		ent3_5.setJoinedDate(createDate(2000, 10, 18));
		ent3_5.setLastTraining(createDate(2000, 10, 19));
		ent3_5.setTaskForce(ent2_2);

		ent2_2.addTaskWorker(ent3_4);
		ent2_2.addTaskWorker(ent3_5);

		em.persist(ent2_1);
		em.persist(ent2_2);

		em.persist(ent3_1);
		em.persist(ent3_2);
		em.persist(ent3_3);
		em.persist(ent3_4);
		em.persist(ent3_5);
		em.persist(expert);
		em.persist(ent2_2);
		em.persist(ent1_1);
		em.persist(ent1_2);

		IMembership membership1 = modelFactory.createMembership();
		IMembershipKey keyId1 = modelFactory.createMembershipKey();
		keyId1.setWorkPlatform(ent1_1);
		keyId1.setUser(user1);
		membership1.setRegistration(createDate(2009, 1, 1));
		membership1.setDiscount(new Double(0.1));
		membership1.setId(keyId1);

		IMembership membership2 = modelFactory.createMembership();
		IMembershipKey keyId2 = modelFactory.createMembershipKey();
		keyId2.setWorkPlatform(ent1_1);
		keyId2.setUser(user2);
		membership2.setRegistration(createDate(2008, 2, 2));
		membership2.setDiscount(new Double(0.2));
		membership2.setId(keyId2);

		IMembership membership3 = modelFactory.createMembership();
		IMembershipKey keyId3 = modelFactory.createMembershipKey();
		keyId3.setWorkPlatform(ent1_2);
		keyId3.setUser(user1);
		membership3.setRegistration(createDate(2007, 3, 3));
		membership3.setDiscount(new Double(0.3));
		membership3.setId(keyId3);

		em.persist(membership1);
		em.persist(membership2);
		em.persist(membership3);

		IMetadata meta1 = modelFactory.createMetadata();
		meta1.setContext("workflow1");
		meta1.setSettings(new ArrayList<String>());

		IMetadata meta2 = modelFactory.createMetadata();
		meta2.setContext("workflow2");
		meta2.setSettings(new ArrayList<String>());

		em.persist(meta1);
		em.persist(meta2);

		ITaskProcessing tp1 = modelFactory.createTaskProcessing();
		tp1.setStart(new Date(System.currentTimeMillis() - 1800000));
		tp1.setStatus(TaskStatus.SCHEDULED);

		ITask t1 = modelFactory.createTask();
		t1.setAssignedWorkUnits(new Integer(3));
		t1.setProcessingTime(new Integer(0));
		t1.setMetadata(meta1);
		t1.setUser(user1);
		t1.setTaskProcessing(tp1);

		tp1.setTask(t1);
		tp1.addWorker(ent3_3);
		ent3_3.addTaskProcessing(tp1);
		em.persist(t1);
		em.persist(ent3_3);

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
