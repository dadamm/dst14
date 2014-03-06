package dst.ass1.nosql;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import dst.ass1.AbstractNoSQLTest;
import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.ITask;

public class Test_5b extends AbstractNoSQLTest {

	@Test
	public void testFindLastUpdatedQuery() throws UnknownHostException,
			MongoException {
		Mongo mongo = new Mongo();
		DB db = mongo.getDB("dst");

		IMongoDbQuery mongoQuery = mongoDbFactory.createQuery(db);
		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());

		List<ITask> list = daoFactory.getTaskDAO().findAll();
		for (ITask o : list) {
			assertEquals(o.getTaskProcessing().getEnd().getTime(), mongoQuery
					.findLastUpdatedByTaskId(o.getId()).longValue());
		}

		mongo.close();
	}

	@Test
	public void testLastUpdatedGt() throws UnknownHostException, MongoException {
		long time = 1325397600;
		DAOFactory daoFactory = new DAOFactory((Session) em.getDelegate());
		Mongo mongo = new Mongo();
		DB db = mongo.getDB("dst");
		IMongoDbQuery mongoQuery = mongoDbFactory.createQuery(db);

		List<ITask> list = daoFactory.getTaskDAO().findAll();
		ArrayList<Long> ids1 = new ArrayList<Long>();
		for (ITask o : list) {
			if (o.getTaskProcessing().getEnd().getTime() > time)
				ids1.add(o.getId());
		}

		List<Long> ids2 = mongoQuery.findLastUpdatedGt(time);

		assertEquals(ids2.size(), ids1.size());

		Collections.sort(ids1);
		Collections.sort(ids2);

		for (int i = 0; i < ids1.size(); i++) {
			assertEquals(ids1.get(i), ids2.get(i));
		}

	}
}
