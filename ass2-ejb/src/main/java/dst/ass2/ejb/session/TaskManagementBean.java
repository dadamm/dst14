package dst.ass2.ejb.session;

import java.util.List;

import dst.ass2.ejb.dto.AssignmentDTO;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.interfaces.ITaskManagementBean;

public class TaskManagementBean implements ITaskManagementBean {

	// TODO

	@Override
	public void addTask(Long platformId, Integer numWorkUnits, String context,
			List<String> settings) throws AssignmentException {
		// TODO
	}

	@Override
	public void login(String username, String password)
			throws AssignmentException {
		// TODO
	}

	@Override
	public void removeTasksForPlatform(Long platformId) {
		// TODO
	}

	@Override
	public void submitAssignments() throws AssignmentException {
		// TODO
	}

	@Override
	public List<AssignmentDTO> getCache() {
		// TODO
		return null;
	}

}
