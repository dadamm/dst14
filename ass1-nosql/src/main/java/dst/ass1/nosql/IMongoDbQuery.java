package dst.ass1.nosql;

import java.util.List;

import com.mongodb.DBObject;

public interface IMongoDbQuery {

	public Long findLastUpdatedByTaskId(Long id);

	public List<Long> findLastUpdatedGt(Long time);

	public List<DBObject> mapReduceWorkflow();

}
