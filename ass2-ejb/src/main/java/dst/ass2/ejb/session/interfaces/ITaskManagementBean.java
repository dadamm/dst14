package dst.ass2.ejb.session.interfaces;

import java.util.List;

import dst.ass2.ejb.dto.AssignmentDTO;
import dst.ass2.ejb.session.exception.AssignmentException;

public interface ITaskManagementBean {

	/**
	 * Adds a task with the given parameters to the temporary 
	 * list if there are enough free work resources left.
	 * 
	 * @param platformId
	 * @param numWorkUnits
	 * @param context
	 * @param settings
	 * @throws AssignmentException
	 *             if the given platform does not exist or if 
	 *             there are not enough free work resources left.
	 */
	public void addTask(Long platformId, Integer numWorkUnits, String context,
			List<String> settings) throws AssignmentException;

	/**
	 * @return the list of temporarily assigned tasks.
	 */
	public List<AssignmentDTO> getCache();

	/**
	 * Removes temporary assigned jobs.
	 */
	public void removeTasksForPlatform(Long id);

	/**
	 * Allows the user to login.
	 * 
	 * @param username
	 * @param password
	 * @throws AssignmentException
	 *             if the user does not exist or the given username/password
	 *             combination does not match.
	 */
	public void login(String username, String password)
			throws AssignmentException;

	/**
	 * Final submission of the temporary assigned jobs.
	 * 
	 * @throws AssignmentException
	 *             if the user is not logged in or one of the jobs can't be
	 *             assigned anymore.
	 */
	public void submitAssignments() throws AssignmentException;

}
