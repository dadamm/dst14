package dst.ass2.ejb;

import static dst.ass2.ejb.util.EJBUtils.lookup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.NoSuchEJBException;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dst.ass2.AbstractEJBTest;
import dst.ass2.ejb.dto.AssignmentDTO;
import dst.ass2.ejb.session.TaskManagementBean;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.interfaces.ITaskManagementBean;
import dst.ass2.util.TaskHelperDTO;

public class Test_TaskManagementBean extends AbstractEJBTest {

	private ITaskManagementBean taskManagementBean;

	@Before
	public void setUp() throws NamingException {
		managementBean.clearPriceCache();
		taskManagementBean = lookup(ctx, TaskManagementBean.class);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testCache() {
		// cache should be empty
		List<AssignmentDTO> cache = taskManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(0, cache.size());
	}

	@Test
	public void testLogin_With_CorrectCredentials() {
		try {
			taskManagementBean.login("hansi", "pw");
		} catch (AssignmentException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		}
	}

	@Test
	public void testLogin_With_InvalidCredentials() {
		try {
			taskManagementBean.login("hansi", "pw1");
			fail("Login with invalid credentials passed. Expected: "
					+ AssignmentException.class.getName());
		} catch (AssignmentException e) {
			// expected exception
		}
	}

	@Test
	public void testAdd_And_RemoveTasks() {
		try {
			// remove all tasks from db, since on task is included in the
			// test-data
			jdbcTestUtil.removeAllEntity5_FROM_DB();

			// get the ids from the database
			List<Long> ids = jdbcTestUtil.getAllEntity1Ids_FROM_DB();
			assertEquals(2, ids.size());

			// add 2 tasks
			taskManagementBean.addTask(ids.get(0), 1, "workflow",
					new ArrayList<String>());
			taskManagementBean.addTask(ids.get(0), 2, "workflow1",
					new ArrayList<String>());

			// there should be two tasks in the cache
			List<AssignmentDTO> cache = taskManagementBean.getCache();
			assertNotNull(cache);
			assertEquals(2, cache.size());
			assertTrue(isTaskInCache(new AssignmentDTO(ids.get(0), 1,
					"workflow", new ArrayList<String>(), null), cache));
			assertTrue(isTaskInCache(new AssignmentDTO(ids.get(0), 2,
					"workflow1", new ArrayList<String>(), null), cache));

			// remove tasks for incorrect value
			taskManagementBean.removeTasksForPlatform(Long.MAX_VALUE);

			// cache should stay the same
			cache = taskManagementBean.getCache();
			assertNotNull(cache);
			assertEquals(2, cache.size());
			assertTrue(isTaskInCache(
					new AssignmentDTO(ids.get(0), Integer.valueOf(1),
							"workflow", new ArrayList<String>(),
							new ArrayList<Long>()), cache));
			assertTrue(isTaskInCache(
					new AssignmentDTO(ids.get(0), Integer.valueOf(2),
							"workflow1", new ArrayList<String>(),
							new ArrayList<Long>()), cache));

			// remove tasks
			taskManagementBean.removeTasksForPlatform(ids.get(0));

			// cache should be empty
			cache = taskManagementBean.getCache();
			assertNotNull(cache);
			assertEquals(0, cache.size());

		} catch (AssignmentException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		}

	}

	@Test
	public void testAddTask_With_Login_And_Check_Discarded() {
		try {
			// remove all tasks from db, since on task is included in the
			// test-data
			jdbcTestUtil.removeAllEntity5_FROM_DB();

			// get the ids from the database
			List<Long> ids = jdbcTestUtil.getAllEntity1Ids_FROM_DB();
			assertEquals(2, ids.size());

			managementBean_addPrices();

			taskManagementBean.login("hansi", "pw");

			List<String> params1 = new ArrayList<String>();
			params1.add("param1");
			params1.add("param2");
			taskManagementBean.addTask(ids.get(0), 2, "workflow1", params1);

			List<String> params2 = new ArrayList<String>();
			params2.add("param3");
			taskManagementBean.addTask(ids.get(1), 6, "workflow2", params2);

			// check cache
			List<AssignmentDTO> cache = taskManagementBean.getCache();
			assertNotNull(cache);
			assertEquals(2, cache.size());
			assertTrue(isTaskInCache(
					new AssignmentDTO(ids.get(0), Integer.valueOf(2),
							"workflow1", params1, new ArrayList<Long>()), cache));
			assertTrue(isTaskInCache(
					new AssignmentDTO(ids.get(1), Integer.valueOf(6),
							"workflow2", params2, new ArrayList<Long>()), cache));

			taskManagementBean.submitAssignments();

			// there have to be 2 tasks in total in the db
			List<TaskHelperDTO> tasks = jdbcTestUtil.getAllEntity5_FROM_DB();

			assertNotNull(tasks);
			assertEquals(2, tasks.size());

			assertTrue(checkTaskInList("workflow1", "hansi", params1, tasks));
			assertTrue(checkTaskInList("workflow2", "hansi", params2, tasks));

			// check if the bean was discarded after submitAssignments() was
			// called successfully!
			try {
				taskManagementBean.getCache();
				fail(NoSuchEJBException.class.getName() + " expected!");
			} catch (NoSuchEJBException e) {
				// Expected
			}

		} catch (AssignmentException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		}

	}

	@Test
	public void testAddTask_With_Login_AnotherUser() {
		try {
			// remove all tasks from db, since on task is included in the
			// test-data
			jdbcTestUtil.removeAllEntity5_FROM_DB();

			// get the ids from the database
			List<Long> ids = jdbcTestUtil.getAllEntity1Ids_FROM_DB();
			assertEquals(2, ids.size());

			managementBean_addPrices();

			taskManagementBean.login("franz", "liebe");

			List<String> params1 = new ArrayList<String>();
			params1.add("param1");
			params1.add("param2");
			params1.add("param3");
			params1.add("param4");

			taskManagementBean.addTask(ids.get(0), 2, "workflow3", params1);

			// check cache
			List<AssignmentDTO> cache = taskManagementBean.getCache();
			assertNotNull(cache);
			assertEquals(1, cache.size());
			assertTrue(isTaskInCache(
					new AssignmentDTO(ids.get(0), Integer.valueOf(2),
							"workflow3", params1, new ArrayList<Long>()), cache));

			taskManagementBean.submitAssignments();

			// there has to be 1 task in total in the db
			List<TaskHelperDTO> tasks = jdbcTestUtil.getAllEntity5_FROM_DB();

			assertNotNull(tasks);
			assertEquals(1, tasks.size());

			assertTrue(checkTaskInList("workflow3", "franz", params1, tasks));

		} catch (AssignmentException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		}
	}

	@Test
	public void testAddTask_Without_Login() {
		try {
			// remove all tasks from db, since on task is included in the
			// test-data
			jdbcTestUtil.removeAllEntity5_FROM_DB();

			// get the ids from the database
			List<Long> ids = jdbcTestUtil.getAllEntity1Ids_FROM_DB();
			assertEquals(2, ids.size());

			managementBean.addPrice(0, new BigDecimal(50));

			List<String> params1 = new ArrayList<String>();
			params1.add("param1");
			params1.add("param2");
			taskManagementBean.addTask(ids.get(0), 2, "workflow1", params1);

			// try to submit assignments => without log in
			taskManagementBean.submitAssignments();

			fail("Assignments got submitted without login. AssignmentException expected!");
		} catch (AssignmentException e) {
			// Expected Exception
		}

		// there shouldn't be any tasks in the db
		assertEquals(0, jdbcTestUtil.countEntity5_FROM_DB());

	}

	@Test
	public void testAddTask_Without_Login2() {
		try {
			// remove all tasks from db, since on task is included in the
			// test-data
			jdbcTestUtil.removeAllEntity5_FROM_DB();

			// get the ids from the database
			List<Long> ids = jdbcTestUtil.getAllEntity1Ids_FROM_DB();
			assertEquals(2, ids.size());

			managementBean_addPrices();

			List<String> params1 = new ArrayList<String>();
			params1.add("param1");
			params1.add("param2");
			taskManagementBean.addTask(ids.get(0), 2, "workflow1", params1);

			List<String> params2 = new ArrayList<String>();
			params2.add("param1");
			taskManagementBean.addTask(ids.get(1), 6, "workflow2", params2);

			// check cache
			List<AssignmentDTO> cache = taskManagementBean.getCache();
			assertNotNull(cache);
			assertEquals(2, cache.size());
			assertTrue(isTaskInCache(
					new AssignmentDTO(ids.get(0), Integer.valueOf(2),
							"workflow1", params1, new ArrayList<Long>()), cache));
			assertTrue(isTaskInCache(
					new AssignmentDTO(ids.get(1), Integer.valueOf(6),
							"workflow2", params2, new ArrayList<Long>()), cache));

			// try to submit assignments => without log in
			try {
				taskManagementBean.submitAssignments();
				fail("Assignments got submitted without login. AssignmentException expected!");
			} catch (AssignmentException e) {
				// Expected Exception
			}

			// check cache => should not be cleared
			cache = taskManagementBean.getCache();
			assertNotNull(cache);
			assertEquals(2, cache.size());
			assertTrue(isTaskInCache(
					new AssignmentDTO(ids.get(0), Integer.valueOf(2),
							"workflow1", params1, new ArrayList<Long>()), cache));
			assertTrue(isTaskInCache(
					new AssignmentDTO(ids.get(1), Integer.valueOf(6),
							"workflow2", params2, new ArrayList<Long>()), cache));

			// there shouldn't be any tasks in the db
			assertEquals(0, jdbcTestUtil.countEntity5_FROM_DB());

			// login
			taskManagementBean.login("hansi", "pw");
			// successfully submit Assignments
			taskManagementBean.submitAssignments();

			// check the database here

			// there have to be 2 tasks in total in the db
			List<TaskHelperDTO> tasks = jdbcTestUtil.getAllEntity5_FROM_DB();

			assertNotNull(tasks);
			assertEquals(2, tasks.size());

			assertTrue(checkTaskInList("workflow1", "hansi", params1, tasks));
			assertTrue(checkTaskInList("workflow2", "hansi", params2, tasks));

		} catch (AssignmentException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		}

	}

	@Test
	public void testAddTask_With_WrongId() {
		// remove all tasks from db, since on task is included in the
		// test-data
		jdbcTestUtil.removeAllEntity5_FROM_DB();

		long nonExisting = Long.MAX_VALUE;

		// try to add a task for non-existing id.
		try {
			taskManagementBean.addTask(nonExisting, 3, "workflow5",
					new ArrayList<String>());
			fail("ID is not available in DB. AssignmentException expected!");
		} catch (AssignmentException e) {
			// expected Exception
		}

		// cache should be empty
		List<AssignmentDTO> cache = taskManagementBean.getCache();
		assertNotNull(cache);
		assertEquals(0, cache.size());

		// also db should be empty
		assertEquals(0, jdbcTestUtil.countEntity5_FROM_DB());
	}

	private boolean checkTaskInList(String context, String username,
			List<String> settings, List<TaskHelperDTO> tasks) {

		for (TaskHelperDTO task : tasks) {
			String taskContext = task.getContext();
			assertNotNull(task.getContext());
			String taskUsername = task.getUsername();
			assertNotNull(taskUsername);
			List<String> taskSettings = task.getSettings();
			assertNotNull(taskSettings);
			assertNotNull(task.getStart());

			if (taskContext.equals(context) && taskUsername.equals(username)
					&& taskSettings.size() == settings.size()
					&& taskSettings.containsAll(settings))
				return true;
		}

		return false;
	}

	private boolean isTaskInCache(AssignmentDTO assignmentDTO,
			List<AssignmentDTO> cache) {
		for (AssignmentDTO cached : cache) {
			Long id1 = cached.getPlatformId();
			Integer num1 = cached.getNumWorkUnits();
			List<String> list1 = cached.getSettings();
			String string1 = cached.getContext();
			List<Long> list2 = cached.getWorkerIds();

			assertNotNull(id1);
			assertNotNull(num1);
			assertNotNull(list1);
			assertNotNull(string1);
			assertNotNull(list2);
			assertTrue(list2.size() > 0);

			if (id1.equals(assignmentDTO.getPlatformId())
					&& num1.equals(assignmentDTO.getNumWorkUnits())
					&& list1.equals(assignmentDTO.getSettings())
					&& string1.equals(assignmentDTO.getContext())) {
				return true;
			}

		}
		return false;
	}
}
