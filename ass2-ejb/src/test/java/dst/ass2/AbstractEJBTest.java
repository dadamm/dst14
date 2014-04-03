package dst.ass2;

import static dst.ass2.ejb.util.EJBUtils.lookup;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;

import dst.ass1.jpa.util.JdbcConnection;
import dst.ass1.jpa.util.JdbcHelper;
import dst.ass2.ejb.session.GeneralManagementBean;
import dst.ass2.ejb.session.TaskManagementBean;
import dst.ass2.ejb.session.TestingBean;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.interfaces.IGeneralManagementBean;
import dst.ass2.ejb.session.interfaces.ITaskManagementBean;
import dst.ass2.ejb.session.interfaces.ITestingBean;
import dst.ass2.util.JDBCTestUtil;

public abstract class AbstractEJBTest {

	protected InitialContext ctx;
	protected JdbcConnection jdbcConnection;
	protected ITestingBean testingBean;
	protected IGeneralManagementBean managementBean;

	protected JDBCTestUtil jdbcTestUtil;

	@Before
	public void init() throws Exception {
		ctx = new InitialContext();
		jdbcConnection = new JdbcConnection();
		
		JdbcHelper.cleanTables(jdbcConnection);

		testingBean = lookup(ctx, TestingBean.class);
		managementBean = lookup(ctx, GeneralManagementBean.class);
		
		testingBean.insertTestData();

		jdbcTestUtil = new JDBCTestUtil(jdbcConnection);
	}

	@After
	public void clean() {
		if (jdbcConnection != null) {
			try {
				jdbcConnection.disconnect();
			} catch (SQLException e) {
			}
		}

		if (ctx != null) {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
	}

	protected void managementBean_clearPrices() {
		managementBean.clearPriceCache();
	}

	protected void managementBean_addPrices() {
		managementBean.addPrice(0, new BigDecimal(50));
		managementBean.addPrice(1, new BigDecimal(45));
		managementBean.addPrice(2, new BigDecimal(40));
		managementBean.addPrice(4, new BigDecimal(35));
		managementBean.addPrice(10, new BigDecimal(30));
		managementBean.addPrice(20, new BigDecimal(20));
		managementBean.addPrice(100, new BigDecimal(15));
	}

	protected void addTasks(List<Long> ids)
			throws NamingException, AssignmentException {
		ITaskManagementBean taskManagementBean = lookup(ctx,
				TaskManagementBean.class);

		taskManagementBean.login("hansi", "pw");

		List<String> settings1 = new ArrayList<String>();
		settings1.add("param1");
		settings1.add("param2");
		taskManagementBean.addTask(ids.get(0), 2, "workflow1", settings1);

		List<String> settings2 = new ArrayList<String>();
		settings2.add("param1");
		taskManagementBean.addTask(ids.get(1), 6, "workflow2", settings2);

		taskManagementBean.submitAssignments();
	}

}
