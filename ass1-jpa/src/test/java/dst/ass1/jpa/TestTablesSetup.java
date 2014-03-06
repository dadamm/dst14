package dst.ass1.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.JdbcHelper;

public class TestTablesSetup extends AbstractTest {

	@Test
	public void testJdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(JdbcHelper.isTable(Constants.J_METADATA_SETTINGS, jdbcConnection));
	}

	@Test
	public void testTables1Jdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(JdbcHelper.isTable(Constants.T_MEMBERSHIP, jdbcConnection));
		assertTrue(JdbcHelper.isColumnInTable(Constants.T_MEMBERSHIP,
				Constants.I_WORKPLATFORM, jdbcConnection));
		assertTrue(JdbcHelper.isColumnInTable(Constants.T_MEMBERSHIP,
				Constants.I_USER, jdbcConnection));
		assertFalse(JdbcHelper.isColumnInTable(Constants.T_WORKPLATFORM,
				Constants.I_MEMBERSHIP, jdbcConnection));
		assertFalse(JdbcHelper.isColumnInTable(Constants.T_USER,
				Constants.I_MEMBERSHIP, jdbcConnection));

	}

	@Test
	public void testTables2Jdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(JdbcHelper.isTable(Constants.J_PROCESSING_TASKWORKER, jdbcConnection));
		assertTrue(JdbcHelper.isColumnInTable(Constants.J_PROCESSING_TASKWORKER,
				Constants.I_PROCESSINGS, jdbcConnection));
		assertTrue(JdbcHelper.isColumnInTable(Constants.J_PROCESSING_TASKWORKER,
				Constants.I_TASKWORKERS, jdbcConnection));
	}

	@Test
	public void testTables3Jdbc() throws ClassNotFoundException,
			SQLException {
		assertFalse(JdbcHelper.isColumnInTable(Constants.T_WORKPLATFORM,
				Constants.I_TASKFORCE, jdbcConnection));
		assertTrue(JdbcHelper.isColumnInTable(Constants.T_TASKFORCE,
				Constants.I_WORKPLATFORM, jdbcConnection));
		assertTrue(JdbcHelper.isIndex(Constants.T_TASKFORCE,
				Constants.I_WORKPLATFORM, true, jdbcConnection));
	}

	@Test
	public void testTables4Jdbc() throws ClassNotFoundException,
			SQLException {
		assertTrue(JdbcHelper.isColumnInTable(Constants.T_TASKWORKER,
				Constants.I_TASKFORCE, jdbcConnection));
		assertFalse(JdbcHelper.isColumnInTable(Constants.T_TASKFORCE,
				Constants.I_TASKWORKER, jdbcConnection));
		assertTrue(JdbcHelper.isIndex(Constants.T_TASKWORKER,
				Constants.I_TASKFORCE, true, jdbcConnection));
	}

	@Test
	public void testTables5Jdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(JdbcHelper.isIndex(Constants.T_TASKFORCE,
				Constants.I_EXPERT, true, jdbcConnection));
		assertTrue(JdbcHelper.isColumnInTable(Constants.T_TASKFORCE,
				Constants.I_EXPERT, jdbcConnection));
		assertFalse(JdbcHelper.isColumnInTable(Constants.T_EXPERT,
				Constants.I_TASKFORCE, jdbcConnection));
	}

	@Test
	public void testTables6Jdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(JdbcHelper.isColumnInTableWithType("User", "password",
				"VARBINARY", "16", jdbcConnection));
	}

}
