package dst.ass1.jpa;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.JdbcHelper;

public class Test_2d extends AbstractTest {
	
	@Test
	public void testUserPasswordIdx() throws SQLException, ClassNotFoundException {
		assertTrue(JdbcHelper.isIndex(Constants.T_USER, "password", true, jdbcConnection));
	}
}
