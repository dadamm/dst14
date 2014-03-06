package dst.ass1.nosql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

import dst.ass1.AbstractNoSQLTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.util.Constants;

public class Test_5a extends AbstractNoSQLTest {

	@Test
	public void testMongoDataLoader() throws Exception {
		MongoTestData mongoTestData = new MongoTestData();
		Mongo mongo = new Mongo();
		DB db = mongo.getDB("dst");
		DBCollection collection = db.getCollection(Constants.COLL_TASKRESULT);
		DBCursor cursor = collection.find();
		ArrayList<DBObject> dbData = new ArrayList<DBObject>();
		while (cursor.hasNext()) {
			dbData.add(cursor.next());
		}
		cursor.close();

		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());
		List<ITask> list = daoFactory.getTaskDAO().findAll();

		assertEquals(list.size(), dbData.size());

		for (ITask o : list) {
			boolean isFound = false;
			for (DBObject obj : dbData) {
				if (Long.valueOf(obj.get(Constants.I_TASK).toString()).equals(
						o.getId())) {
					isFound = true;
					assertEquals(o.getTaskProcessing().getEnd().getTime(), Long
							.valueOf(obj.get("last_updated").toString())
							.longValue());
					String desc = mongoTestData.getDataDesc(o.getId()
							.intValue());
					assertEquals(obj.get(desc), JSON.parse(mongoTestData
							.getStringData(o.getId().intValue())));
				}
			}
			assertTrue(isFound);
		}
	}

}
