package dst.ass2.ejb;

import static dst.ass2.ejb.util.EJBUtils.lookup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dst.ass2.AbstractEJBTest;
import dst.ass2.ejb.dto.BillDTO;
import dst.ass2.ejb.dto.BillDTO.BillPerTask;
import dst.ass2.ejb.session.TaskManagementBean;
import dst.ass2.ejb.session.interfaces.ITaskManagementBean;
import dst.ass2.ejb.util.JdbcHelper;

public class Test_Bill extends AbstractEJBTest {

	@Before
	public void setUp() {
		managementBean.clearPriceCache();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testGetBill_forUser_Hansi() {
		try {
			// get all available ids from db
			List<Long> ids = jdbcTestUtil.getAllEntity1Ids_FROM_DB();

			assertEquals(2, ids.size());

			// add prices to management bean
			managementBean_addPrices();

			// add task
			addTasks(ids);

			// finish all tasks on db level!
			JdbcHelper.finishAllTasks(jdbcConnection);

			Future<BillDTO> result = managementBean.getBillForUser("hansi");
			while (!result.isDone()) {
				Thread.sleep(100);
			}

			// gather all paid tasks for user
			List<Long> paid = jdbcTestUtil
					.getPaidEntity5ForUser_FROM_DB("hansi");

			assertEquals(3, paid.size());

			BillDTO billDTO = result.get();
			assertNotNull(billDTO);

			List<BillPerTask> bills = billDTO.getBills();
			assertNotNull(bills);
			assertEquals(3, bills.size());

			Map<Long, BillPerTask> temp = createMap(bills);

			BillPerTask bill = temp.get(paid.get(0));

			assertNotNull(bill);
			assertNotNull(bill.getNumberOfWorkers());
			assertTrue(bill.getNumberOfWorkers().intValue() >= 1);
			assertNotNull(bill.getSetupCosts());

			// In case discount was considered only for the total price
			assertTrue(bill.getSetupCosts().compareTo(new BigDecimal(45.00)) == 0
					|| bill.getSetupCosts().compareTo(new BigDecimal(50.00)) == 0);

			assertNotNull(bill.getProcessingCosts());

			// In case discount was considered only for the total price
			assertTrue(bill.getProcessingCosts().compareTo(
					new BigDecimal(135.00)) == 0
					|| bill.getProcessingCosts().compareTo(
							new BigDecimal(150.00)) == 0);

			assertNotNull(bill.getTaskCosts());
			assertTrue(bill.getTaskCosts().compareTo(new BigDecimal(180.00)) == 0);

			bill = temp.get(paid.get(1));

			assertNotNull(bill);
			assertNotNull(bill.getNumberOfWorkers());
			assertTrue(bill.getNumberOfWorkers().intValue() >= 1);
			assertNotNull(bill.getSetupCosts());

			// In case discount was considered only for the total price
			assertTrue(bill.getSetupCosts().compareTo(new BigDecimal(40.50)) == 0
					|| bill.getSetupCosts().compareTo(new BigDecimal(45.00)) == 0);

			assertNotNull(bill.getProcessingCosts());
			assertTrue(bill.getProcessingCosts()
					.compareTo(new BigDecimal(0.00)) == 0);
			assertNotNull(bill.getTaskCosts());

			assertTrue(bill.getTaskCosts().compareTo(new BigDecimal(40.50)) == 0);

			bill = temp.get(paid.get(2));

			assertNotNull(bill);
			assertNotNull(bill.getNumberOfWorkers());
			assertTrue(bill.getNumberOfWorkers().intValue() >= 1);
			assertNotNull(bill.getSetupCosts());

			// In case discount was considered only for the total price
			assertTrue(bill.getSetupCosts().compareTo(new BigDecimal(28.00)) == 0
					|| bill.getSetupCosts().compareTo(new BigDecimal(40.00)) == 0);

			assertNotNull(bill.getProcessingCosts());

			assertTrue(bill.getProcessingCosts()
					.compareTo(new BigDecimal(0.00)) == 0);
			assertNotNull(bill.getTaskCosts());

			assertTrue(bill.getTaskCosts().compareTo(new BigDecimal(28.00)) == 0);

			assertNotNull(billDTO.getTotalPrice());
			assertTrue(billDTO.getTotalPrice()
					.compareTo(new BigDecimal(248.50)) == 0);
			assertNotNull(billDTO.getUsername());
			assertTrue(billDTO.getUsername().equals("hansi"));

		} catch (Exception e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		}

	}

	@Test
	public void testGetBill_forUser_Franz() {
		try {
			// get all available ids from db
			List<Long> ids = jdbcTestUtil.getAllEntity1Ids_FROM_DB();

			assertEquals(2, ids.size());

			// add prices to management bean
			managementBean_addPrices();

			// add tasks for user franz
			ITaskManagementBean taskManagementBean = lookup(ctx,
					TaskManagementBean.class);

			taskManagementBean.login("franz", "liebe");

			List<String> params3 = new ArrayList<String>();
			params3.add("param1");
			params3.add("param2");
			params3.add("param3");
			params3.add("param4");
			taskManagementBean.addTask(ids.get(0), new Integer(2), "workflow3",
					params3);

			List<String> params5 = new ArrayList<String>();

			taskManagementBean.addTask(ids.get(0), new Integer(3), "workflow5",
					params5);

			taskManagementBean.submitAssignments();

			// finish all tasks on db level!
			JdbcHelper.finishAllTasks(jdbcConnection);

			Future<BillDTO> result = managementBean.getBillForUser("franz");

			while (!result.isDone()) {
				Thread.sleep(100);
			}

			// gather all paid tasks for user
			List<Long> paid = jdbcTestUtil
					.getPaidEntity5ForUser_FROM_DB("franz");
			assertEquals(2, paid.size());

			BillDTO billDTO = result.get();
			assertNotNull(billDTO);

			List<BillPerTask> bills = billDTO.getBills();
			assertNotNull(bills);

			assertEquals(2, bills.size());

			Map<Long, BillPerTask> temp = createMap(bills);

			BillPerTask bill = temp.get(paid.get(0));

			assertNotNull(bill);
			assertNotNull(bill.getNumberOfWorkers());
			assertTrue(bill.getNumberOfWorkers().intValue() >= 1);
			assertNotNull(bill.getSetupCosts());

			// In case discount was considered only for the total price
			assertTrue(bill.getSetupCosts().compareTo(new BigDecimal(40.00)) == 0
					|| bill.getSetupCosts().compareTo(new BigDecimal(50.00)) == 0);

			assertNotNull(bill.getProcessingCosts());
			assertTrue(bill.getProcessingCosts()
					.compareTo(new BigDecimal(0.00)) == 0);
			assertNotNull(bill.getTaskCosts());

			assertTrue(bill.getTaskCosts().compareTo(new BigDecimal(40.00)) == 0);

			bill = temp.get(paid.get(1));

			assertNotNull(bill);
			assertNotNull(bill.getNumberOfWorkers());
			assertTrue(bill.getNumberOfWorkers().intValue() >= 1);
			assertNotNull(bill.getSetupCosts());

			// In case discount was considered only for the total price
			assertTrue(bill.getSetupCosts().compareTo(new BigDecimal(36.00)) == 0
					|| bill.getSetupCosts().compareTo(new BigDecimal(45.00)) == 0);
			
			assertNotNull(bill.getProcessingCosts());
			assertTrue(bill.getProcessingCosts()
					.compareTo(new BigDecimal(0.00)) == 0);
			assertNotNull(bill.getTaskCosts());
			
			assertTrue(bill.getTaskCosts().compareTo(new BigDecimal(36.00)) == 0);

			assertNotNull(billDTO.getTotalPrice());
			assertTrue(billDTO.getTotalPrice().compareTo(new BigDecimal(76.00)) == 0);
			assertNotNull(billDTO.getUsername());
			assertTrue(billDTO.getUsername().equals("franz"));

		} catch (Exception e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		}
	}

	@Test
	public void testGetBill_ForNotExistingUser() {
		String username = "not_existing_user";

		try {
			Future<BillDTO> result = managementBean.getBillForUser(username);

			while (!result.isDone()) {
				Thread.sleep(100);
			}

			result.get();

			fail(String.format("User: %s should not exist!", username));
		} catch (Exception e) {
			// Expected exception since user should not exist!
		}

	}

	private Map<Long, BillPerTask> createMap(List<BillPerTask> bills) {
		Map<Long, BillPerTask> ret = new HashMap<Long, BillPerTask>();

		for (BillPerTask bill : bills)
			ret.put(bill.getTaskId(), bill);

		return ret;
	}

}
