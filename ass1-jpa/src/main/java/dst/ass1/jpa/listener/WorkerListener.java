package dst.ass1.jpa.listener;

import java.util.Date;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

import dst.ass1.jpa.model.impl.TaskWorker;

public class WorkerListener {
	
	@PostPersist
	public void afterSaveWorker(TaskWorker taskWorker) {
		taskWorker.setJoinedDate(new Date());
		taskWorker.setLastTraining(new Date());
	}
	
	@PostUpdate
	public void afterUpdateWorker(TaskWorker taskWorker) {
		taskWorker.setLastTraining(new Date());
	}
	
}
