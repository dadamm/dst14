package dst.ass1.nosql.impl;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import dst.ass1.jpa.util.Constants;
import dst.ass1.nosql.IMongoDbQuery;

public class MongoDbQuery implements IMongoDbQuery {
	
	private DB db;
	
	public MongoDbQuery(DB db) {
		this.db = db;
	}

	@Override
	public Long findLastUpdatedByTaskId(Long id) {
		DBCollection collection = db.getCollection(Constants.COLL_TASKRESULT);
		BasicDBObject obj = new BasicDBObject("task_id", id);
		
		DBCursor cursor = collection.find(obj);
		if(cursor.hasNext()) {
			return (Long) cursor.next().get("last_updated");
		} else {
			System.out.println("cannot find task_id: " + id);
			return -1L;
		}
	}

	@Override
	public List<Long> findLastUpdatedGt(Long time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DBObject> mapReduceWorkflow() {
		// TODO Auto-generated method stub
		return null;
	}

}
