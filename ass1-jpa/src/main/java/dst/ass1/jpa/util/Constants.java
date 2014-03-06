package dst.ass1.jpa.util;

public class Constants {

	/* TYPES (CLASSES) */
	public static final String T_TASKWORKER = "TaskWorker";
	public static final String T_TASKFORCE = "TaskForce";
	public static final String T_WORKPLATFORM = "WorkPlatform";
	public static final String T_USER = "User";
	public static final String T_MEMBERSHIP = "Membership";
	public static final String T_TASK = "Task";
	public static final String T_EXPERT = "Expert";
	public static final String T_TASKPROCESSING = "TaskProcessing";
	public static final String T_METADATA = "Metadata";
	public static final String T_PERSON = "Person";

	/* IDs (FOREIGN KEYS) */
	public static final String I_TASKFORCE = T_TASKFORCE.toLowerCase() + "_id";
	public static final String I_WORKPLATFORM = T_WORKPLATFORM.toLowerCase() + "_id";
	public static final String I_EXPERT = T_EXPERT.toLowerCase() + "_id";
	public static final String I_USER = T_USER.toLowerCase() + "_id";
	public static final String I_MEMBERSHIP = T_MEMBERSHIP.toLowerCase() + "_id";
	public static final String I_TASKWORKER = T_TASKWORKER.toLowerCase() + "_id";
	public static final String I_TASKWORKERS = "taskworkers_id";
	public static final String I_PROCESSING = T_TASKPROCESSING.toLowerCase() + "_id";
	public static final String I_PROCESSINGS = "taskprocessings_id";
	public static final String I_TASK = "task_id";
	public static final String I_METADATA = "metadata_id";

	/* MEMBER ATTRIBUTES */
	public static final String M_WORKUNITCAPACITY = "workUnitCapacity";
	public static final String M_COSTSPERWORKUNIT = "costsPerWorkUnit";
	public static final String M_LASTTRAINING = "lastTraining";
	public static final String M_LASTMEETING = "lastMeeting";
	public static final String M_NEXTMEETING = "nextMeeting";
	public static final String M_JOINEDDATE = "joinedDate";
	public static final String M_ISPAID = "isPaid";
	public static final String M_EXPERT = "expert";
	public static final String M_SETTINGS_ORDER = "settings_ORDER";
	public static final String M_LOCATION = "location";
	public static final String M_CONTEXT = "context";

	/* ASSOCIATION NAMES (FOR QUERIES) */
	public static final String A_WORKPLATFORM = "workPlatform";
	public static final String A_PROCESSING = "taskProcessing";
	public static final String A_PROCESSINGS = "taskProcessings";
	public static final String A_TASKWORKERS = "taskWorkers";
	public static final String A_TASKFORCE = "taskForce";
	public static final String A_TASKFORCES = "taskForces";
	public static final String A_MEMBERSHIPS = "memberships";
	public static final String A_TASKS = "tasks";
	public static final String A_TASK = "task";
	public static final String A_USER = "user";
	public static final String A_METADATA = "metadata";

	/* NAMED QUERIES */
	public static final String Q_ALLFINISHEDTASKS = "allFinishedTasks";
	public static final String Q_TASKFORCESOFEXPERT = "taskforcesOfExpert";
	public static final String Q_USERSWITHACTIVEMEMBERSHIP = "usersWithActiveMembership";
	public static final String Q_MOSTACTIVEUSER = "mostActiveUser";

	/* JOIN TABLES */
	public static final String J_PROCESSING_TASKWORKER = "processing_taskworker";
	public static final String J_METADATA_SETTINGS = "Metadata_settings";

	/* COLLECTION NAMES (for NoSQL part) */
	public static final String COLL_TASKRESULT = "TaskResult";

}