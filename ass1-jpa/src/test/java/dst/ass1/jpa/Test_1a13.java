package dst.ass1.jpa;

import static junit.framework.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.JdbcHelper;

public class Test_1a13 extends AbstractTest {

	@Test
	public void testAssociationJdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(JdbcHelper.isTable(Constants.J_PROCESSING_TASKWORKER, jdbcConnection));
		assertTrue(JdbcHelper.isColumnInTable(Constants.J_PROCESSING_TASKWORKER,
				Constants.I_PROCESSINGS, jdbcConnection));
		assertTrue(JdbcHelper.isColumnInTable(Constants.J_PROCESSING_TASKWORKER,
				Constants.I_TASKWORKERS, jdbcConnection));
	}

}
