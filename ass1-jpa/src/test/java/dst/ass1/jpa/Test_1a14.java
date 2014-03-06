package dst.ass1.jpa;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.JdbcHelper;

public class Test_1a14 extends AbstractTest {

	@Test
	public void testAssociationJdbc() throws ClassNotFoundException,
			SQLException {
		assertFalse(JdbcHelper.isColumnInTable(Constants.T_WORKPLATFORM,
				Constants.I_TASKFORCE, jdbcConnection));
		assertTrue(JdbcHelper.isColumnInTable(Constants.T_TASKFORCE,
				Constants.I_WORKPLATFORM, jdbcConnection));
		assertTrue(JdbcHelper.isIndex(Constants.T_TASKFORCE,
				Constants.I_WORKPLATFORM, true, jdbcConnection));
	}

}
