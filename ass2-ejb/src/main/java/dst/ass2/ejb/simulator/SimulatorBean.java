package dst.ass2.ejb.simulator;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst.ass1.jpa.model.ITask;
import dst.ass1.jpa.model.ITaskProcessing;
import dst.ass1.jpa.model.TaskStatus;

@Singleton
@Startup
public class SimulatorBean {
	
	@Resource TimerService timerService;
	@PersistenceContext private EntityManager entityManager;
	
	private Integer getProcessingTime(Date start, Date end) {
		Long result = end.getTime() - start.getTime();
		return result.intValue();
	}
	
	@Schedule(hour = "*", minute = "*", second = "*/5")
	public void simulate() {
		
		@SuppressWarnings("unchecked")
		List<ITask> tasks = entityManager.createQuery("select t from Task t join t.taskProcessing tp where tp.end is null and tp.start < CURRENT_TIMESTAMP").getResultList();
		
		Date currentDate = new Date();
		
		for(ITask task : tasks) {
			ITaskProcessing taskProcessing = task.getTaskProcessing();
			taskProcessing.setEnd(currentDate);
			taskProcessing.setStatus(TaskStatus.FINISHED);
			task.setProcessingTime(getProcessingTime(taskProcessing.getStart(), taskProcessing.getEnd()));
			entityManager.persist(task);
		}
	}

}
