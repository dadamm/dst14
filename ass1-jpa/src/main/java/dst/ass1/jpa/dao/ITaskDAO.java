package dst.ass1.jpa.dao;

import dst.ass1.jpa.model.ITask;

import java.util.Date;
import java.util.List;

public interface ITaskDAO extends GenericDAO<ITask> {
	List<ITask> findTasksForUserAndContext(String user, String c);
	List<ITask> findTasksForStatusFinishedStartandFinish(Date start,Date finish);
}
