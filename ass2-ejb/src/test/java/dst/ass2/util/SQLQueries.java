package dst.ass2.util;

public class SQLQueries {

	// --- WORKPLATFORM ---
	protected final static String ALL_PLATFORM_IDS = "SELECT id FROM Workplatform order by id ASC";
	protected final static String SQL_GET_ALL_PAID_TASKS_FOR_USER = "SELECT id, isPaid FROM Task t "
			+ "WHERE t.user_id = (SELECT id FROM User WHERE username=?) AND isPaid = 1 order by id ASC";

	// --- PRICE ---
	protected final static String SQL_PRICE_TABLE_NAME = "Price";
	protected final static String SQL_IS_PRICE_AVAILABLE = "SELECT COUNT(*) FROM Price WHERE nrOfHistoricalTasks = ? AND price = ?";
	protected final static String SQL_COUNT_PRICES = "SELECT COUNT(*) FROM Price";

	// --- TASK ---
	protected final static String SQL_TASK_TABLE_NAME = "Task";
	protected final static String SQL_COUNT_TASKS = "SELECT COUNT(*) FROM " + SQL_TASK_TABLE_NAME;
	protected final static String SQL_GET_ALL_TASKS = "SELECT t.id, m.context, p.start, u.username FROM Task t, Metadata m, TaskProcessing p, User u "
			+ "WHERE t.metadata_id = m.id AND t.taskprocessing_id = p.id AND t.user_id = u.id";
	protected final static String SQL_GET_SETTINGS_FOR_TASK = "SELECT ms.settings FROM Task t , Metadata m, Metadata_Settings ms "
			+ "WHERE t.id = ? and m.id = t.metadata_id and m.id=ms.Metadata_id order by settings_order asc";

	// --- TASKPROCESSING ---
	protected final static String SQL_PROCESSING_TABLE_NAME = "TaskProcessing";
	protected final static String SQL_COUNT_FINISHED_PROCESSING = "SELECT COUNT(*) FROM " + SQL_PROCESSING_TABLE_NAME + " p "
			+ "WHERE p.start IS NOT NULL AND p.end IS NOT NULL AND p.status = 'FINISHED'";

	// --- EXPERT ---
	protected final static String SQL_COUNT_EXPERT_1 = "SELECT COUNT(*) FROM Expert e, Person p "
			+ "WHERE e.id = p.id AND p.city = ? AND p.street = ? AND p.zipCode = ? AND p.firstName = ? AND p.lastname = ?";
	protected final static String SQL_COUNT_EXPERT_2 = "SELECT COUNT(*) FROM Expert "
			+ "WHERE city = ? AND street = ? AND zipCode = ? AND firstName = ? AND lastname = ?";

	// --- TASKFORCE ---
	protected final static String SQL_COUNT_TASKFORCE = "SELECT COUNT(*) FROM TaskForce "
			+ "WHERE lastMeeting IS NOT NULL AND nextMeeting IS NOT NULL AND name = ?";

	// --- TASKWORKER ---
	protected final static String SQL_COUNT_TASKWORKER = "SELECT COUNT(*) from TaskWorker "
			+ "WHERE workUnitCapacity = ? AND joinedDate IS NOT NULL AND lastTraining IS NOT NULL AND location = ? AND name = ?";

	// --- METADATA ---
	protected final static String SQL_COUNT_METADATA = "SELECT COUNT(*) FROM Metadata WHERE context = ?";

	// --- TASKPROCESSING ---
	protected final static String SQL_COUNT_PROCESSING = "SELECT COUNT(*) FROM TaskProcessing WHERE start IS NOT NULL";

	// --- WORKPLATFORM ---
	protected final static String SQL_COUNT_WORKPLATFORM = "SELECT COUNT(*) FROM WorkPlatform WHERE costsPerWorkUnit = ? AND location = ? AND name = ?";

	// --- TASK ---
	protected final static String SQL_COUNT_TASK = "SELECT COUNT(*) FROM Task WHERE isPaid = ?";

	// --- MEMBERSHIP ---
	protected final static String SQL_COUNT_MEMBERSHIP = "SELECT COUNT(*) FROM Membership "
			+ "WHERE discount = ? AND registration IS NOT NULL";

	// --- USER ---
	protected final static String SQL_COUNT_USER_1 = "SELECT COUNT(*) FROM User u, Person p "
			+ "WHERE u.id = p.id AND p.city = ? AND p.street = ? AND p.zipCode = ? AND p.firstName = ? "
			+ "AND p.lastName = ? AND u.accountNo = ? AND u.bankCode = ? AND u.username = ? AND u.password = ?";

	protected final static String SQL_COUNT_USER_2 = "SELECT COUNT(*) FROM User "
			+ "WHERE city = ? AND street = ? AND zipCode = ? AND firstName = ? "
			+ "AND lastName = ? AND accountNo = ? AND bankCode = ? AND username = ? AND password = ?";

}
