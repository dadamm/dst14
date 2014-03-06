package dst.ass1.jpa.util.test;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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

public class TestData {

	public static final String N_ENT1_1 = "platform1";
	public static final String N_ENT1_2 = "platform2";
	public static final String N_ENT2_1 = "taskforce1";
	public static final String N_ENT2_2 = "taskforce2";
	public static final String N_ENT2_3 = "taskforce3";
	public static final String N_ENT3_1 = "worker1";
	public static final String N_ENT3_2 = "worker2";
	public static final String N_ENT3_3 = "worker3";
	public static final String N_ENT3_4 = "worker4";
	public static final String N_ENT6_1 = "context1";
	public static final String N_ENT6_2 = "context2";
	public static final String N_ENT6_3 = "context3";
	public static final String N_ENT6_4 = "context4";
	public static final String N_ENT7_1 = "Alex";
	public static final String N_ENT7_2 = "Alexandra";
	public static final String N_ENT7_3 = "Alexander";
	public static final String N_USER_1 = "user1";
	public static final String N_USER_2 = "user2";

	public Long entity7_1Id;
	public Long entity7_2Id;
	public Long entity7_3Id;

	public Long user1Id;
	public Long user2Id;

	public Long entity3_1_Id;
	public Long entity3_2_Id;
	public Long entity3_3_Id;

	public Long entity2_1_Id;
	public Long entity2_2_Id;

	public Long entity5_1_Id;
	public Long entity5_2_Id;
	public Long entity5_3_Id;

	private EntityManager em;

	public TestData(EntityManager em) {
		this.em = em;
	}

	public void insertTestData() throws NoSuchAlgorithmException {

		ModelFactory modelFactory = new ModelFactory();

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		// Addresses
		IAddress address1 = modelFactory.createAddress();
		IAddress address2 = modelFactory.createAddress();
		IAddress address3 = modelFactory.createAddress();
		IAddress address4 = modelFactory.createAddress();
		IAddress address5 = modelFactory.createAddress();

		address1.setCity("city1");
		address1.setStreet("street1");
		address1.setZipCode("zip1");

		address2.setCity("city2");
		address2.setStreet("street2");
		address2.setZipCode("zip2");

		address3.setCity("city3");
		address3.setStreet("street3");
		address3.setZipCode("zip3");

		address4.setCity("city4");
		address4.setStreet("street4");
		address4.setZipCode("zip4");

		address4.setCity("city5");
		address4.setStreet("street5");
		address4.setZipCode("zip5");

		IExpert ent7_1 = modelFactory.createExpert();
		IExpert ent7_2 = modelFactory.createExpert();
		IExpert ent7_3 = modelFactory.createExpert();

		((IPerson) ent7_1).setFirstName(TestData.N_ENT7_1);
		((IPerson) ent7_1).setLastName(TestData.N_ENT7_1);
		((IPerson) ent7_1).setAddress(address1);

		((IPerson) ent7_2).setFirstName(TestData.N_ENT7_2);
		((IPerson) ent7_2).setLastName(TestData.N_ENT7_2);
		((IPerson) ent7_2).setAddress(address2);

		((IPerson) ent7_3).setFirstName(TestData.N_ENT7_3);
		((IPerson) ent7_3).setLastName(TestData.N_ENT7_3);
		((IPerson) ent7_3).setAddress(address5);

		// Users
		MessageDigest md = MessageDigest.getInstance("MD5");

		IUser user1 = modelFactory.createUser();
		((IPerson) user1).setFirstName("first1");
		((IPerson) user1).setLastName("last1");
		((IPerson) user1).setAddress(address3);
		user1.setAccountNo("account1");
		user1.setUsername(dst.ass1.jpa.util.test.TestData.N_USER_1);
		user1.setBankCode("bank1");
		user1.setPassword(md.digest("pw1".getBytes()));

		IUser user2 = modelFactory.createUser();
		((IPerson) user2).setFirstName("first2");
		((IPerson) user2).setLastName("last2");
		((IPerson) user2).setAddress(address4);
		user2.setAccountNo("account2");
		user2.setUsername(dst.ass1.jpa.util.test.TestData.N_USER_2);
		user2.setBankCode("bank2");
		user2.setPassword(md.digest("pw2".getBytes()));

		em.persist(ent7_1);
		em.persist(ent7_2);
		em.persist(ent7_3);
		em.persist(user1);
		em.persist(user2);

		// Entity #3
		ITaskWorker ent3_1 = modelFactory.createTaskWorker();
		ent3_1.setName(N_ENT3_1);
		ent3_1.setWorkUnitCapacity(2);
		ent3_1.setLocation("AUT-VIE-location1");
		ent3_1.setJoinedDate(new Date(0));
		ent3_1.setLastTraining(new Date(0));

		ITaskWorker ent3_2 = modelFactory.createTaskWorker();
		ent3_2.setName(N_ENT3_2);
		ent3_2.setWorkUnitCapacity(3);
		ent3_2.setLocation("AUT-VIE-location2");
		ent3_2.setJoinedDate(new Date(0));
		ent3_2.setLastTraining(new Date(0));

		ITaskWorker ent3_3 = modelFactory.createTaskWorker();
		ent3_3.setName(N_ENT3_3);
		ent3_3.setWorkUnitCapacity(4);
		ent3_3.setLocation("AUT-VIE-location3");
		ent3_3.setJoinedDate(new Date(0));
		ent3_3.setLastTraining(new Date(0));

		ITaskWorker ent3_4 = modelFactory.createTaskWorker();
		ent3_4.setName(N_ENT3_4);
		ent3_4.setWorkUnitCapacity(4);
		ent3_4.setLocation("AUT-IBK-location4");
		ent3_4.setJoinedDate(new Date(0));
		ent3_4.setLastTraining(new Date(0));

		// Entity #2
		ITaskForce ent2_1 = modelFactory.createTaskForce();
		ent2_1.setExpert(ent7_1);
		ent2_1.setName(N_ENT2_1);
		ent2_1.setLastMeeting(new Date(0));
		ent2_1.setNextMeeting(new Date(0));

		ITaskForce ent2_2 = modelFactory.createTaskForce();
		ent2_2.setExpert(ent7_2);
		ent2_2.setName(N_ENT2_2);
		ent2_2.setLastMeeting(new Date(1));
		ent2_2.setNextMeeting(new Date(1));

		ITaskForce ent2_3 = modelFactory.createTaskForce();
		ent2_3.setExpert(ent7_3);
		ent2_3.setName(N_ENT2_3);
		ent2_3.setLastMeeting(new Date(2));
		ent2_3.setNextMeeting(new Date(2));

		ent2_1.addComposedOf(ent2_2);
		ent2_2.addPartOf(ent2_1);

		ent7_1.addAdvisedTaskForce(ent2_1);
		ent7_2.addAdvisedTaskForce(ent2_2);

		ent2_1.addTaskWorker(ent3_1);
		ent2_1.addTaskWorker(ent3_2);
		ent3_1.setTaskForce(ent2_1);
		ent3_2.setTaskForce(ent2_1);

		ent2_2.addTaskWorker(ent3_3);
		ent3_3.setTaskForce(ent2_2);

		ent2_3.addTaskWorker(ent3_4);
		ent3_4.setTaskForce(ent2_3);

		// Entity #1

		IWorkPlatform ent1_1 = modelFactory.createPlatform();
		ent1_1.setName(N_ENT1_1);
		ent1_1.setLocation("vienna");
		ent1_1.setCostsPerWorkUnit(new BigDecimal(20));

		ent1_1.addTaskForce(ent2_1);
		ent1_1.addTaskForce(ent2_2);

		ent2_1.setWorkPlatform(ent1_1);
		ent2_2.setWorkPlatform(ent1_1);

		IWorkPlatform ent1_2 = modelFactory.createPlatform();
		ent1_2.setName(N_ENT1_2);
		ent1_2.setLocation("vienna");
		ent1_2.setCostsPerWorkUnit(new BigDecimal(20));

		ent1_2.addTaskForce(ent2_3);

		ent2_3.setWorkPlatform(ent1_2);

		em.persist(ent1_1);
		em.persist(ent1_2);

		// Memberships
		IMembership membership1 = modelFactory.createMembership();
		membership1.setDiscount(0.10);
		membership1.setRegistration(new Date());

		IMembershipKey key1 = modelFactory.createMembershipKey();
		key1.setUser(user1);
		key1.setWorkPlatform(ent1_1);

		membership1.setId(key1);
		user1.addMembership(membership1);

		IMembership membership2 = modelFactory.createMembership();
		membership2.setDiscount(0.10);
		membership2.setRegistration(new Date());

		IMembershipKey key2 = modelFactory.createMembershipKey();
		key2.setUser(user2);
		key2.setWorkPlatform(ent1_1);

		membership2.setId(key2);
		user2.addMembership(membership2);

		IMembership membership3 = modelFactory.createMembership();
		membership3.setDiscount(0.10);
		membership3.setRegistration(new Date());

		IMembershipKey key3 = modelFactory.createMembershipKey();
		key3.setUser(user1);
		key3.setWorkPlatform(ent1_2);

		membership3.setId(key3);
		user1.addMembership(membership3);

		// Entity #6
		IMetadata ent6_1 = modelFactory.createMetadata();
		ent6_1.setContext(TestData.N_ENT6_1);
		ent6_1.addSetting("param1");
		ent6_1.addSetting("param2");
		ent6_1.addSetting("param3");

		IMetadata ent6_2 = modelFactory.createMetadata();
		ent6_2.setContext(TestData.N_ENT6_2);
		ent6_2.addSetting("param4");

		IMetadata ent6_3 = modelFactory.createMetadata();
		ent6_3.setContext(TestData.N_ENT6_3);
		ent6_3.addSetting("param5");

		IMetadata ent6_4 = modelFactory.createMetadata();
		ent6_4.setContext(TestData.N_ENT6_4);
		ent6_4.addSetting("param6");

		em.persist(ent6_1);
		em.persist(ent6_2);
		em.persist(ent6_3);
		em.persist(ent6_4);

		ITaskProcessing ex1 = modelFactory.createTaskProcessing();
		ex1.setStart(new Date(System.currentTimeMillis() - 36000000));
		ex1.setEnd(new Date());
		ex1.setStatus(TaskStatus.SCHEDULED);

		ITaskProcessing ex2 = modelFactory.createTaskProcessing();
		ex2.setStart(new Date());
		ex2.setStatus(TaskStatus.SCHEDULED);

		ITaskProcessing ex3 = modelFactory.createTaskProcessing();
		ex3.setStart(new Date());
		ex3.setStatus(TaskStatus.SCHEDULED);

		ITaskProcessing ex4 = modelFactory.createTaskProcessing();
		ex4.setStart(new Date());
		ex4.setStatus(TaskStatus.SCHEDULED);

		ex1.addWorker(ent3_1);
		ex2.addWorker(ent3_2);
		ex3.addWorker(ent3_3);
		ex4.addWorker(ent3_4);

		ent3_1.addTaskProcessing(ex1);
		ent3_2.addTaskProcessing(ex2);
		ent3_3.addTaskProcessing(ex3);
		ent3_4.addTaskProcessing(ex4);

		// Entity #5
		ITask ent5_1 = modelFactory.createTask();
		ent5_1.setAssignedWorkUnits(2);
		ent5_1.setProcessingTime(0);
		ent5_1.setMetadata(ent6_1);
		ent5_1.setTaskProcessing(ex1);
		ent5_1.setUser(user1);
		user1.addTask(ent5_1);

		ITask ent5_2 = modelFactory.createTask();
		ent5_2.setAssignedWorkUnits(3);
		ent5_2.setProcessingTime(0);
		ent5_2.setMetadata(ent6_2);
		ent5_2.setTaskProcessing(ex2);
		ent5_2.setUser(user2);
		user2.addTask(ent5_2);

		ITask ent5_3 = modelFactory.createTask();
		ent5_3.setAssignedWorkUnits(4);
		ent5_3.setProcessingTime(0);
		ent5_3.setMetadata(ent6_3);
		ent5_3.setTaskProcessing(ex3);
		ent5_3.setUser(user1);
		user1.addTask(ent5_3);

		ITask ent5_4 = modelFactory.createTask();
		ent5_4.setAssignedWorkUnits(4);
		ent5_4.setProcessingTime(0);
		ent5_4.setMetadata(ent6_4);
		ent5_4.setTaskProcessing(ex4);
		ent5_4.setUser(user1);
		user1.addTask(ent5_4);

		ex1.setTask(ent5_1);
		ex2.setTask(ent5_2);
		ex3.setTask(ent5_3);
		ex4.setTask(ent5_4);

		em.persist(ent7_1);
		em.persist(ent7_2);
		em.persist(ent7_3);
		em.persist(user1);
		em.persist(user2);
		em.persist(ent2_1);
		em.persist(ent2_2);
		em.persist(ent2_3);
		em.persist(ent3_1);
		em.persist(ent3_2);
		em.persist(ent3_3);
		em.persist(ent3_4);
		em.persist(ent1_1);
		em.persist(ent1_2);
		em.persist(membership1);
		em.persist(membership2);
		em.persist(membership3);
		em.persist(ent5_1);
		em.persist(ent5_2);
		em.persist(ent5_3);
		em.persist(ent5_4);

		tx.commit();

		entity7_1Id = ((IPerson) ent7_1).getId();
		entity7_2Id = ((IPerson) ent7_2).getId();
		entity7_3Id = ((IPerson) ent7_3).getId();

		user1Id = ((IPerson) user1).getId();
		user2Id = ((IPerson) user2).getId();

		entity3_1_Id = ent3_1.getId();
		entity3_2_Id = ent3_2.getId();
		entity3_3_Id = ent3_3.getId();

		entity2_1_Id = ent2_1.getId();
		entity2_2_Id = ent2_2.getId();

		entity5_1_Id = ent5_1.getId();
		entity5_2_Id = ent5_2.getId();
		entity5_3_Id = ent5_3.getId();

	}

}
