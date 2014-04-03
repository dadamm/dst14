package dst.ass1.nosql.impl;

import java.util.List;

import javax.persistence.EntityManager;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

import dst.ass1.jpa.model.impl.Task;
import dst.ass1.jpa.util.Constants;
import dst.ass1.nosql.IMongoDbDataLoader;
import dst.ass1.nosql.MongoTestData;

public class MongoDbDataLoader implements IMongoDbDataLoader {
	
	private EntityManager em;
	private MongoTestData testData;
	
	public MongoDbDataLoader(EntityManager em) {
		this.em = em;
		testData = new MongoTestData();
	}
	
	private String createJsonString(Long taskid, Long lastUpdated) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"task_id\" : ");
		sb.append(taskid);
		sb.append(" , ");
		sb.append("\"last_updated\" : ");
		sb.append(lastUpdated);
		sb.append(" , ");
		sb.append(testData.getDataDesc(taskid.intValue()));
		sb.append(" : ");
		sb.append(testData.getStringData(taskid.intValue()));
		sb.append(" , ");
		sb.append("\"type\" : ");
		sb.append("\"identity_matrix\"");
		sb.append("}");
		return sb.toString();
	}
	
	@Override
	public void loadData() throws Exception {
		
		@SuppressWarnings("unchecked")
		List<Task> tasks = em.createNamedQuery(Constants.Q_ALLFINISHEDTASKS).getResultList();
		
		Mongo mongo = new Mongo();
		DB db = mongo.getDB("dst");
		DBCollection collection = db.getCollection(Constants.COLL_TASKRESULT);
		
		collection.createIndex(new BasicDBObject(Constants.I_TASK, 1));
		
		for(Task task : tasks) {
			String jsonString = createJsonString(task.getId(), task.getTaskProcessing().getEnd().getTime());
			collection.insert((DBObject) JSON.parse(jsonString));
		}
		
		mongo.close();
	}

}
