package dst.ass2.util;

import static dst.ass2.util.SQLQueries.*;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.JdbcConnection;
import dst.ass1.jpa.util.JdbcHelper;

public class JDBCTestUtil {

	private JdbcConnection jdbcConnection;

	public JDBCTestUtil(JdbcConnection jdbcConnection) {
		this.jdbcConnection = jdbcConnection;
	}

	private Statement createStatement() throws SQLException {
		return jdbcConnection.getConnection().createStatement();
	}

	private void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				/*
				 * Note: When a Statement object is closed, its current
				 * ResultSet object, if one exists, is also closed.
				 */
				statement.close();
			} catch (SQLException e) {
			}
		}
	}

	public List<Long> getAllEntity1Ids_FROM_DB() {
		List<Long> ids = new ArrayList<Long>();
		Statement stmt = null;

		try {
			stmt = createStatement();
			ResultSet rs = stmt.executeQuery(ALL_PLATFORM_IDS);

			while (rs.next())
				ids.add(rs.getLong(1));

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(stmt);
		}

		return ids;
	}

	public List<Long> getPaidEntity5ForUser_FROM_DB(String user) {
		List<Long> list = new ArrayList<Long>();

		PreparedStatement pstmt = null;

		try {
			pstmt = jdbcConnection.getConnection().prepareStatement(
					SQL_GET_ALL_PAID_TASKS_FOR_USER);
			pstmt.setString(1, user);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(rs.getLong("id"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(pstmt);
		}

		return list;
	}

	public int countPrices_FROM_DB() {
		int ret = 0;

		Statement stmt = null;

		try {
			stmt = createStatement();
			ResultSet rs = stmt.executeQuery(SQL_COUNT_PRICES);

			if (rs.next())
				ret = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(stmt);
		}

		return ret;
	}

	public boolean isPrice_IN_DB(Integer numHistorical, BigDecimal price) {
		boolean ret = false;

		PreparedStatement pstmt = null;

		try {
			pstmt = jdbcConnection.getConnection().prepareStatement(
					SQL_IS_PRICE_AVAILABLE);
			pstmt.setInt(1, numHistorical.intValue());
			pstmt.setBigDecimal(2, price);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				int count = rs.getInt(1);

				if (count == 1)
					ret = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(pstmt);
		}

		return ret;
	}

	public void removeAllPrices_FROM_DB() {
		truncateTable_IN_DB(SQL_PRICE_TABLE_NAME);
	}

	public int countEntity5_FROM_DB() {
		int ret = -1;

		Statement stmt = null;

		try {
			stmt = createStatement();
			ResultSet rs = stmt.executeQuery(SQL_COUNT_TASKS);

			if (rs.next())
				ret = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(stmt);
		}

		return ret;
	}

	public List<TaskHelperDTO> getAllEntity5_FROM_DB() {
		List<TaskHelperDTO> list = new ArrayList<TaskHelperDTO>();

		Statement stmt = null;

		try {
			stmt = createStatement();
			ResultSet rs = stmt.executeQuery(SQL_GET_ALL_TASKS);

			while (rs.next()) {
				// j.id, env.workflow, ex.start, u.username
				Long id = rs.getLong("id");
				String context = rs.getString(Constants.M_CONTEXT);
				Date start = rs.getDate("start");
				String username = rs.getString("username");

				list.add(new TaskHelperDTO(id, context, start, username, null));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(stmt);
		}

		for (TaskHelperDTO ent : list) {
			// get parameters
			List<String> list1 = getListForEntity5_FROM_DB(ent.getId());
			ent.setSettings(list1);
		}

		return list;
	}

	public void removeAllEntity5_FROM_DB() {
		truncateTable_IN_DB(SQL_TASK_TABLE_NAME);
		truncateTable_IN_DB(SQL_PROCESSING_TABLE_NAME);
	}

	private void truncateTable_IN_DB(String table) {
		Statement stmt = null;
		try {
			stmt = createStatement();
			stmt.addBatch("SET FOREIGN_KEY_CHECKS=0");
			stmt.addBatch("truncate table " + table);
			stmt.addBatch("SET FOREIGN_KEY_CHECKS=1");
			stmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(stmt);
		}
	}

	private List<String> getListForEntity5_FROM_DB(Long id) {
		List<String> parameters = new ArrayList<String>();

		PreparedStatement pstmt = null;

		try {
			pstmt = jdbcConnection.getConnection().prepareStatement(
					SQL_GET_SETTINGS_FOR_TASK);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next())
				parameters.add(rs.getString(1));

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(pstmt);
		}

		return parameters;
	}

	public int countFinishedEntity4_FROM_DB() {
		int ret = 0;

		Statement stmt = null;

		try {
			stmt = createStatement();
			ResultSet rs = stmt.executeQuery(SQL_COUNT_FINISHED_PROCESSING);

			if (rs.next())
				ret = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(stmt);
		}

		return ret;
	}

	public int countEntity7_FROM_DB(String city, String street,
			String zipCode, String firstName, String lastName) {
		int ret = 0;

		PreparedStatement pstmt = null;

		try {
			int type = JdbcHelper.getInheritanceType(jdbcConnection, "Person");
			switch (type) {
			case 0:
				pstmt = jdbcConnection.getConnection().prepareStatement(
						SQL_COUNT_EXPERT_1);
				break;
			case 1:
				pstmt = jdbcConnection.getConnection().prepareStatement(
						SQL_COUNT_EXPERT_2);
				break;
			default:
				fail("Unknown inheritance type is used!");
			}

			pstmt.setString(1, city);
			pstmt.setString(2, street);
			pstmt.setString(3, zipCode);
			pstmt.setString(4, firstName);
			pstmt.setString(5, lastName);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next())
				ret = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(pstmt);
		}

		return ret;
	}

	public int countEntity2_FROM_DB(String name) {
		int ret = 0;

		PreparedStatement pstmt = null;

		try {
			pstmt = jdbcConnection.getConnection().prepareStatement(
					SQL_COUNT_TASKFORCE);
			pstmt.setString(1, name);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next())
				ret = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(pstmt);
		}

		return ret;
	}

	public int coungEntity3_FROM_DB(int numWorkUnits, String location,
			String name) {
		int ret = 0;

		PreparedStatement pstmt = null;

		try {
			pstmt = jdbcConnection.getConnection().prepareStatement(
					SQL_COUNT_TASKWORKER);
			pstmt.setInt(1, numWorkUnits);
			pstmt.setString(2, location);
			pstmt.setString(3, name);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next())
				ret = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(pstmt);
		}

		return ret;
	}

	public int countEntity6_FROM_DB(String context) {
		int ret = 0;

		PreparedStatement pstmt = null;

		try {
			pstmt = jdbcConnection.getConnection().prepareStatement(
					SQL_COUNT_METADATA);
			pstmt.setString(1, context);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next())
				ret = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(pstmt);
		}

		return ret;
	}

	public int countEntity4_FROM_DB() {
		int ret = 0;

		Statement stmt = null;

		try {
			stmt = createStatement();
			ResultSet rs = stmt.executeQuery(SQL_COUNT_PROCESSING);

			if (rs.next())
				ret = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(stmt);
		}

		return ret;
	}

	public int countEntity1_FROM_DB(BigDecimal costsPerUnit,
			String location, String name) {
		int ret = 0;

		PreparedStatement pstmt = null;

		try {
			pstmt = jdbcConnection.getConnection().prepareStatement(
					SQL_COUNT_WORKPLATFORM);
			pstmt.setBigDecimal(1, costsPerUnit);
			pstmt.setString(2, location);
			pstmt.setString(3, name);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next())
				ret = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(pstmt);
		}

		return ret;
	}

	public int countEntity5_FROM_DB(boolean isPaid) {
		int ret = 0;

		PreparedStatement pstmt = null;

		try {
			pstmt = jdbcConnection.getConnection().prepareStatement(
					SQL_COUNT_TASK);
			pstmt.setBoolean(1, isPaid);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next())
				ret = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(pstmt);
		}

		return ret;
	}

	public int countMemberships_FROM_DB(Double discount) {
		int ret = 0;

		PreparedStatement pstmt = null;

		try {
			pstmt = jdbcConnection.getConnection().prepareStatement(
					SQL_COUNT_MEMBERSHIP);
			pstmt.setDouble(1, discount);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next())
				ret = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(pstmt);
		}

		return ret;

	}

	public int countUsers_FROM_DB(String city, String street,
			String zipCode, String firstName, String lastName, String bankCode,
			String accountNo, String userName, byte[] password) {
		int ret = 0;

		PreparedStatement pstmt = null;

		try {
			int type = JdbcHelper.getInheritanceType(jdbcConnection, "Person");
			switch (type) {
			case 0:
				pstmt = jdbcConnection.getConnection().prepareStatement(
						SQL_COUNT_USER_1);
				break;
			case 1:
				pstmt = jdbcConnection.getConnection().prepareStatement(
						SQL_COUNT_USER_2);
				break;
			default:
				fail("Unknown inheritance type is used!");
			}

			pstmt.setString(1, city);
			pstmt.setString(2, street);
			pstmt.setString(3, zipCode);
			pstmt.setString(4, firstName);
			pstmt.setString(5, lastName);
			pstmt.setString(6, bankCode);
			pstmt.setString(7, accountNo);
			pstmt.setString(8, userName);
			pstmt.setBytes(9, password);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next())
				ret = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail(String.format("Unexpected Exception: %s !", e.getMessage()));
		} finally {
			closeStatement(pstmt);
		}

		return ret;
	}
}
