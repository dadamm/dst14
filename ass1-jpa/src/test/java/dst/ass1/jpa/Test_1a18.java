package dst.ass1.jpa;

import static junit.framework.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.util.JdbcHelper;

public class Test_1a18 extends AbstractTest {

	@Test
	public void testAssociationJdbc()
			throws ClassNotFoundException, SQLException {
		assertTrue(JdbcHelper.isColumnInTableWithType("User", "password",
				"VARBINARY", "16", jdbcConnection));
	}
}
