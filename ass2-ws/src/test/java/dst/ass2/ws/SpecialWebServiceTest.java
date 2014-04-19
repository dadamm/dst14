package dst.ass2.ws;

import static dst.ass2.ejb.util.EJBUtils.lookup;
import static dst.ass2.ws.util.MiscUtils.filterAuditLogs;
import static dst.ass2.ws.util.MiscUtils.filterExecutionDtos;
import static dst.ass2.ws.util.MiscUtils.validateAuditLog;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.NoSuchEJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dst.ass1.jpa.util.JdbcConnection;
import dst.ass1.jpa.util.JdbcHelper;
import dst.ass1.jpa.util.test.TestData;
import dst.ass2.ejb.dto.AssignmentDTO;
import dst.ass2.ejb.dto.AuditLogDTO;
import dst.ass2.ejb.dto.BillDTO;
import dst.ass2.ejb.session.GeneralManagementBean;
import dst.ass2.ejb.session.TaskManagementBean;
import dst.ass2.ejb.session.TestingBean;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.exception.WebServiceException;
import dst.ass2.ejb.session.interfaces.IGeneralManagementBean;
import dst.ass2.ejb.session.interfaces.ITaskManagementBean;
import dst.ass2.ejb.session.interfaces.ITaskStatisticsBean;
import dst.ass2.ejb.session.interfaces.ITestingBean;
import dst.ass2.ejb.ws.Constants;
import dst.ass2.ejb.ws.IGetStatsRequest;
import dst.ass2.ejb.ws.IGetStatsResponse;
import dst.ass2.ejb.ws.WSRequestFactory;
import dst.ass2.ws.util.WebServiceUtils;

/**
 * Test scenario for the service implementation.
 */
public class SpecialWebServiceTest {
	static Long id;
	static Context ctx;
	long startTime = System.currentTimeMillis();

	private static JdbcConnection jdbcConnection;

	@BeforeClass
	public static void before() throws Exception {
		ctx = new InitialContext();
		jdbcConnection = new JdbcConnection();
		JdbcHelper.cleanTables(jdbcConnection);
	}

	@AfterClass
	public static void after() throws Exception {
		ctx.close();
		if (jdbcConnection != null)
			jdbcConnection.disconnect();

	}

	@Test
	public void testScenario() throws Exception {
		// Wait a little bit to reduce possible impacts of other tests
		Thread.sleep(1000);

		IGeneralManagementBean generalManagementBean = lookup(ctx,
				GeneralManagementBean.class);

		init();

		// Add some prices
		addPriceSteps(generalManagementBean);

		// Schedule some
		addTasks();

		// Schedule more
		addAdditionalTasks();

		// Wait for the result
		Thread.sleep(10000);

		// Retrieve the bill
		getBill(generalManagementBean);

		// Look at the audit log
		getAudits(generalManagementBean);

		// Do a web service call
		webService();
	}

	private void init() {
		try {
			ITestingBean testingBean = lookup(ctx, TestingBean.class);
			testingBean.insertTestData();
			id = JdbcHelper.getWorkPlatformIds(jdbcConnection).get(0);
		} catch (Exception e) {
			// May occur if test data has already been inserted
		}
	}

	private void addPriceSteps(IGeneralManagementBean generalManagementBean) {
		// Add some price steps
		generalManagementBean.addPrice(0, new BigDecimal(10));
		generalManagementBean.addPrice(1, new BigDecimal(5));
		generalManagementBean.addPrice(2, new BigDecimal(2));
		for (int i = 3; i < 10; i++) {
			generalManagementBean.addPrice(i, new BigDecimal(1));
		}
	}

	private void addTasks() throws NamingException {
		ITaskManagementBean managementBean = lookup(ctx,
				TaskManagementBean.class);

		// Login with invalid username and password
		try {
			managementBean.login("", "");
			throw new RuntimeException(
					AssignmentException.class.getSimpleName() + " expected");
		} catch (AssignmentException e) {
			// expected
		}

		// Login with valid username and password
		try {
			managementBean.login("hansi", "pw");
		} catch (AssignmentException e) {
			fail(ExceptionUtils.getMessage(e));
		}

		// Add first
		try {
			managementBean
					.addTask(id, 4, "1", Collections.<String> emptyList());
		} catch (AssignmentException e) {
			fail(ExceptionUtils.getMessage(e));
		}

		// Add second
		try {
			managementBean.addTask(id, 4, "2", Arrays.asList("a", "b"));
		} catch (AssignmentException e) {
			fail(ExceptionUtils.getMessage(e));
		}

		List<AssignmentDTO> assignments = managementBean.getCache();
		assertEquals("Exactly 2 assignments expected", 2, assignments.size());

		// submit
		try {
			managementBean.submitAssignments();
		} catch (AssignmentException e) {
			fail(ExceptionUtils.getMessage(e));
		}

		// Resubmit
		try {
			managementBean.submitAssignments();
			fail(NoSuchEJBException.class.getSimpleName() + " expected");
		} catch (NoSuchEJBException e) {
			// Expected since the previous call of submitAssignment() should
			// discard the bean!!!
		} catch (AssignmentException e) {
			fail(ExceptionUtils.getMessage(e));
		}
	}

	private void addAdditionalTasks() throws NamingException,
			AssignmentException {
		ITaskManagementBean managementBean = lookup(ctx,
				TaskManagementBean.class);

		// Login with another valid username and password
		managementBean.login("franz", "liebe");

		// Add
		try {
			managementBean.addTask(id, Integer.MAX_VALUE, "max",
					Collections.<String> emptyList());
			fail(AssignmentException.class.getSimpleName() + " expected");
		} catch (AssignmentException e) {
			// expected
		}

		// Clear shopping cart
		managementBean.removeTasksForPlatform(id);
		assertTrue("The shopping cart must be empty", managementBean.getCache()
				.isEmpty());

		// Start
		managementBean.submitAssignments();
	}

	private void getBill(IGeneralManagementBean generalManagementBean)
			throws Exception {
		// Request bill for hansi
		Future<BillDTO> future = generalManagementBean.getBillForUser("hansi");
		BillDTO bill = future.get();

		// Verify price
		assertEquals(new BigDecimal("150.30"), bill.getTotalPrice());
	}

	private void getAudits(IGeneralManagementBean generalManagementBean)
			throws InterruptedException {
		List<AuditLogDTO> audits = filterAuditLogs(
				generalManagementBean.getAuditLogs(), startTime);
		for (int i = 0; audits.size() != 11 && i < 10; i++) {
			// If necessary, give some more time to retrieve the correct amount
			// of audit logs
			Thread.sleep(1000);
			audits = filterAuditLogs(generalManagementBean.getAuditLogs(),
					startTime);
		}
		assertEquals("Expected 11 audit log entries", 11, audits.size());

		validateAuditLog(audits.get(0), "login",
				AssignmentException.class.getSimpleName(), "", "");
		validateAuditLog(audits.get(1), "login", null, "hansi", "pw");
		validateAuditLog(audits.get(2), "addTask", null, id.toString(), "4",
				"1", "[]");
		validateAuditLog(audits.get(3), "addTask", null, id.toString(), "4",
				"2", "[a, b]");
		validateAuditLog(audits.get(4), "getCache", "2");
		validateAuditLog(audits.get(5), "submitAssignments", null);
		validateAuditLog(audits.get(6), "login", null, "franz", "liebe");
		validateAuditLog(audits.get(7), "addTask",
				AssignmentException.class.getSimpleName(), id.toString(),
				"2147483647", "max", "[]");
		validateAuditLog(audits.get(8), "removeTasksForPlatform", null,
				id.toString());
		validateAuditLog(audits.get(9), "getCache", "[]");
		validateAuditLog(audits.get(10), "submitAssignments", null);
	}

	private void webService() {
		// Create Web service client
		ITaskStatisticsBean service = WebServiceUtils.getServiceProxy(
				ITaskStatisticsBean.class, Constants.NAMESPACE,
				Constants.SERVICE_NAME, Constants.SERVICE_WSDL_URL);
		WSRequestFactory factory = new WSRequestFactory();

		// Retrieving stats
		try {
			IGetStatsRequest request1 = factory.createGetStatsRequest();
			request1.setMaxProcessings(10);
			IGetStatsResponse stats = service.getStatisticsForPlatform(
					request1, TestData.N_ENT1_1);
			assertEquals("The name must be '" + TestData.N_ENT1_1 + "'",
					TestData.N_ENT1_1, stats.getStatistics().getName());
			assertEquals(
					"There must be 2 executions",
					2,
					filterExecutionDtos(stats.getStatistics().getProcessings(),
							startTime).size());
		} catch (WebServiceException e1) {
			fail("Unexpected exception when requesting statistics for '"
					+ TestData.N_ENT1_1 + "': " + e1);
		}

		// Retrieving stats 2
		try {
			IGetStatsRequest request2 = factory.createGetStatsRequest();
			request2.setMaxProcessings(10);
			IGetStatsResponse stats = service.getStatisticsForPlatform(
					request2, TestData.N_ENT1_2);
			assertEquals("The name of the statistics must be '"
					+ TestData.N_ENT1_2 + "'", TestData.N_ENT1_2, stats
					.getStatistics().getName());
			assertEquals(
					"There must be 0 executions", 0,
					filterExecutionDtos(stats.getStatistics().getProcessings(),
							startTime).size());
		} catch (WebServiceException e1) {
			fail("Unexpected exception when requesting statistics for '"
					+ TestData.N_ENT1_2 + "': " + e1);
		}

		// Retrieving stats for Unknown
		try {
			IGetStatsRequest request3 = factory.createGetStatsRequest();
			request3.setMaxProcessings(10);
			service.getStatisticsForPlatform(request3, "invalid");
			fail("Exception expected when requesting statistics for unknown.");
		} catch (Exception e) {
			// expected
		}
	}
}
