package dst.ass1;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.TaskStatus;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.JdbcConnection;
import dst.ass1.jpa.util.JdbcHelper;
import dst.ass1.nosql.IMongoDbDataLoader;
import dst.ass1.nosql.IMongoDbQuery;
import dst.ass1.nosql.MongoDbFactory;
import dst.ass1.nosql.TestData;

public abstract class AbstractNoSQLTest {

	private EntityManagerFactory emf;
	private DAOFactory daoFactory;
	
	private JdbcConnection jdbcConnection;

	protected MongoDbFactory mongoDbFactory;
	protected EntityManager em;
	protected IMongoDbDataLoader mongoDbDataLoader;
	protected IMongoDbQuery mongoDbQuery;

	@Before
	public void setUp() throws Exception {
		emf = Persistence.createEntityManagerFactory("dst_pu");
		em = emf.createEntityManager();
		daoFactory = new DAOFactory((Session) em.getDelegate());
		
		jdbcConnection = new JdbcConnection();
		JdbcHelper.cleanTables(jdbcConnection);
		
		(new TestData(em)).insertTestData();

		Mongo mongo = new Mongo();
		DB db = mongo.getDB("dst");
		DBCollection collection = db.getCollection(Constants.COLL_TASKRESULT);
		collection.drop();
		mongo.close();

		mongoDbFactory = new MongoDbFactory();
		mongoDbDataLoader = mongoDbFactory.createDataLoader(em);

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		List<ITask> list = daoFactory.getTaskDAO().findAll();
		for (ITask j : list) {
			j.getTaskProcessing().setEnd(new Date());
			Thread.sleep(1000);
			j.getTaskProcessing().setStatus(TaskStatus.FINISHED);
			em.persist(j);
		}

		tx.commit();

		mongoDbDataLoader.loadData();
	}

	@After
	public void tearDown() throws Exception {
		em.clear();
		em.close();
		emf.close();
		
		JdbcHelper.cleanTables(jdbcConnection);
		jdbcConnection.disconnect();

		Mongo mongo = new Mongo();
		DB db = mongo.getDB("dst");
		DBCollection collection = db.getCollection(Constants.COLL_TASKRESULT);
		collection.drop();
		mongo.close();
	}

}