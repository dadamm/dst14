package dst.ass1.jpa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ##########################
 * 
 * DO NOT CHANGE THIS CLASS!
 * 
 * ##########################
 * 
 * Establishes a JDBC connection to the database.
 */
public class JdbcConnection {

	private final String DRIVER = "org.h2.Driver";

	private Connection jdbcConnection = null;

	public JdbcConnection() {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cannot load driver: " + DRIVER, e);
		}
	}

	/**
	 * Attempts do close the current connection and create a new one with the same parameters.
	 *
	 * @return the new established connection
	 * @throws SQLException if a database access error occurs
	 * @see #disconnect()
	 * @see #connect()
	 */
	public Connection getConnection() throws SQLException {
		try {
			disconnect();
		} catch (Exception e) {
			// ignore any exception of a broken connection
		}
		connect();
		return jdbcConnection;
	}

	/**
	 * Establishes a new database connection.
	 *
	 * @throws SQLException if a database access error occurs
	 */
	private void connect() throws SQLException {
		jdbcConnection = DriverManager.getConnection("jdbc:h2:/tmp/database/dst;AUTO_SERVER=TRUE;MVCC=true", "sa", "");
	}

	/**
	 * Closes the current database connection if available.
	 *
	 * @throws SQLException if a database access error occurs
	 */
	public void disconnect() throws SQLException {
		if (null != jdbcConnection) {
			jdbcConnection.close();
			jdbcConnection = null;
		}
	}

}
