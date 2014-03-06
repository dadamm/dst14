package dst.ass1.jpa.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * ##########################
 * 
 * DO NOT CHANGE THIS CLASS!
 * 
 * ##########################
 * 
 * Contains various convenience methods for database access.
 * <p/>
 * <b>Note that the caller is responsible for dealing with possible exceptions
 * as well as doing the connection handling.</b><br/>
 * In other words, a connection will not be closed even if a fatal error occurs.
 * However, other SQL resources i.e., {@link Statement Statements} and
 * {@link ResultSet ResultSets} created within the methods, which are not
 * returned to the caller, are closed before the method returns.
 */
public final class JdbcHelper {
	private JdbcHelper() {
	}

	/**
	 * Checks if the named table can be accessed via the given
	 * {@link JdbcConnection}.
	 * 
	 * @param tableName
	 *            the name of the table to find
	 * @param jdbcConnection
	 *            the JDBC connection to use
	 * @return {@code true} if the database schema contains a table with the
	 *         given name, {@code false} otherwise
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static boolean isTable(String tableName,
			JdbcConnection jdbcConnection) throws SQLException {
		String sql = "show tables";
		Statement stmt = jdbcConnection.getConnection().createStatement();
		try {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String tbl = rs.getString(1);
				if (tbl.equalsIgnoreCase(tableName)) {
					return true;
				}
			}
		} finally {
			stmt.close();
		}

		return false;
	}

	/**
	 * Detects the type of table inheritance.
	 * 
	 * @param jdbcConnection
	 *            the JDBC connection to use
	 * @param tableName
	 *            the name of the table
	 * @return {@code 0} if the given table exists, {@code 1} otherwise.
	 * @throws SQLException
	 */
	public static int getInheritanceType(JdbcConnection jdbcConnection,
			String tableName) throws SQLException {
		return isTable(tableName, jdbcConnection) ? 0 : 1;
	}

	/**
	 * Checks whether a certain database table contains a column with the given
	 * name.
	 * 
	 * @param tableName
	 *            the name of the table to check
	 * @param column
	 *            the name of the column to find
	 * @param jdbcConnection
	 *            the JDBC connection to use
	 * @return {@code true} if the table contains the column, {@code false}
	 *         otherwise
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static boolean isColumnInTable(String tableName, String column,
			JdbcConnection jdbcConnection) throws SQLException {
		String sql = String
				.format("SELECT * FROM information_schema.columns WHERE table_name='%s' and column_name='%s'",
						tableName.toUpperCase(), column.toUpperCase());
		Statement stmt = jdbcConnection.getConnection().createStatement();
		try {
			return stmt.executeQuery(sql).next();
		} finally {
			stmt.close();
		}
	}

	public static boolean isColumnInTableWithType(String tableName,
			String column, String type, String length,
			JdbcConnection jdbcConnection) throws SQLException {
		String sql = String
				.format("SELECT * FROM information_schema.columns WHERE table_name='%s' and column_name='%s' and type_name='%s' and character_maximum_length='%s'",
						tableName.toUpperCase(), column.toUpperCase(),
						type.toUpperCase(), length);
		Statement stmt = jdbcConnection.getConnection().createStatement();
		try {
			return stmt.executeQuery(sql).next();
		} finally {
			stmt.close();
		}
	}

	/**
	 * Checks whether a certain table contains an index for the given column
	 * name.
	 * 
	 * @param tableName
	 *            the name of the table to check
	 * @param indexName
	 *            the name of the column the index is created for
	 * @param nonUnique
	 *            {@code true} if the index is non unique, {@code false}
	 *            otherwise
	 * @param jdbcConnection
	 *            the JDBC connection to use
	 * @return {@code true} if the index exists, {@code false} otherwise
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static boolean isIndex(String tableName, String indexName,
			boolean nonUnique, JdbcConnection jdbcConnection)
			throws SQLException {
		String sql = String
				.format("SELECT * FROM information_schema.indexes WHERE table_name='%s' and column_name='%s' and non_unique='%s'",
						tableName.toUpperCase(), indexName.toUpperCase(),
						nonUnique ? "1" : "0");

		Statement stmt = jdbcConnection.getConnection().createStatement();
		try {
			return stmt.executeQuery(sql).next();
		} finally {
			stmt.close();
		}
	}

	public static boolean isComposedIndex(String tableName, String columnName1,
			String columnName2, JdbcConnection jdbcConnection)
			throws SQLException {

		String index_name1 = getIndexName(tableName, columnName1,
				jdbcConnection);

		String index_name2 = getIndexName(tableName, columnName2,
				jdbcConnection);

		if (index_name1 == null || index_name2 == null)
			return false;

		if (index_name1.equals(index_name2))
			return true;

		return false;
	}

	private static String getIndexName(String tableName, String columnName,
			JdbcConnection jdbcConnection) throws SQLException {
		String sql_index = String
				.format("SELECT index_name FROM information_schema.indexes WHERE table_name='%s' and column_name='%s'",
						tableName.toUpperCase(), columnName.toUpperCase());

		Statement stmt = jdbcConnection.getConnection().createStatement();
		String index_name = null;
		try {
			ResultSet rs = stmt.executeQuery(sql_index);
			if (rs.next()) {
				index_name = rs.getString(1);
			}
		} finally {
			stmt.close();
		}
		return index_name;
	}

	/**
	 * Checks whether the given column of a certain table can contain
	 * {@code NULL} values.
	 * 
	 * @param tableName
	 *            the name of the table to check
	 * @param columnName
	 *            the name of the column to check
	 * @param jdbcConnection
	 *            the JDBC connection to use
	 * @return {@code true} if the column is nullable, {@code false} otherwise
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static boolean isNullable(String tableName, String columnName,
			JdbcConnection jdbcConnection) throws SQLException {

		String sql = String
				.format("SELECT * FROM information_schema.columns WHERE table_name='%s' and column_name='%s' and IS_NULLABLE=true",
						tableName.toUpperCase(), columnName.toUpperCase());

		Statement stmt = jdbcConnection.getConnection().createStatement();
		try {
			return stmt.executeQuery(sql).next();
		} finally {
			stmt.close();
		}
	}

	/**
	 * Deletes all data from all tables that can be accessed via the given
	 * {@link JdbcConnection}.
	 * 
	 * @param jdbcConnection
	 *            the JDBC connection to use
	 * @throws Exception
	 *             if a database access error occurs
	 */
	public static void cleanTables(JdbcConnection jdbcConnection)
			throws Exception {
		List<String> tables = getTables(jdbcConnection);

		Statement stmt = jdbcConnection.getConnection().createStatement();
		stmt.addBatch("SET FOREIGN_KEY_CHECKS=0");
		for (String table : tables) {
			if (table.toLowerCase().startsWith("hibernate"))
				continue;
			stmt.addBatch("truncate table " + table);
		}
		stmt.addBatch("SET FOREIGN_KEY_CHECKS=1");
		stmt.executeBatch();

		stmt.close();
	}

	/**
	 * Returns a list of all table-names for the given database/connection
	 * 
	 * @param jdbcConnection
	 *            the JDBC connection to use
	 * @return List of table names
	 * @throws Exception
	 *             if a database access error occurs
	 */
	public static List<String> getTables(JdbcConnection jdbcConnection)
			throws Exception {
		Statement stmt = jdbcConnection.getConnection().createStatement();
		String sql = "show tables";
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList<String> tables = new ArrayList<String>();
		while (rs.next()) {
			tables.add(rs.getString(1));
		}
		rs.close();

		return tables;
	}

	/**
	 * Returns the amount of work units currently available.
	 * 
	 * @param jdbcConnection
	 *            the JDBC connection to use
	 * @param id
	 *            the identifier of the requested WorkPlatform
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static int getAvailableWorkUnits(JdbcConnection jdbcConnection,
			String id) throws SQLException {
		int result = 0;
		String sql = "SELECT SUM(" + Constants.M_WORKUNITCAPACITY + ") "
				+ "FROM " + Constants.T_TASKWORKER + " " + "WHERE "
				+ Constants.I_TASKFORCE + "=(" + "SELECT id FROM "
				+ Constants.T_TASKFORCE + " " + "WHERE "
				+ Constants.I_WORKPLATFORM + "=" + id + " LIMIT 0,1)";
		Statement stmt = jdbcConnection.getConnection().createStatement();
		try {
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} finally {
			stmt.close();
		}

		return result;

	}

	/**
	 * Returns the identifiers of existing WorkPlatforms.
	 * 
	 * @param jdbcConnection
	 *            the JDBC connection to use
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static List<Long> getWorkPlatformIds(JdbcConnection jdbcConnection)
			throws SQLException {
		List<Long> ids = new ArrayList<Long>();
		String sql = "SELECT id FROM " + Constants.T_WORKPLATFORM;
		Statement stmt = jdbcConnection.getConnection().createStatement();
		try {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ids.add(rs.getLong(1));
			}
		} finally {
			stmt.close();
		}
		return ids;
	}
}
