package dst.ass2.ejb.util;

import java.sql.SQLException;
import java.sql.Statement;

import dst.ass1.jpa.util.JdbcConnection;

/**
 * Helper functions.
 */
public class JdbcHelper {

	/**
	 * Finishes all tasks currently executed or scheduled for execution.
	 * 
	 * @param jdbcConnection
	 *            the JDBC connection to use
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static void finishAllTasks(JdbcConnection jdbcConnection)
			throws SQLException {
		String sql = "UPDATE TaskProcessing SET END=now() , status='FINISHED'";
		Statement stmt = jdbcConnection.getConnection().createStatement();
		try {
			stmt.executeUpdate(sql);
		} finally {
			stmt.close();
		}
	}

}
