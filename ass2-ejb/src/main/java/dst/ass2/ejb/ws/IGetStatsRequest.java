package dst.ass2.ejb.ws;

/**
 * This interface defines the getters and setters of the 
 * GetStatsRequest Web service request object.
 */
public interface IGetStatsRequest {

	/**
	 * @return maximum number of processings in the statistics
	 */
	int getMaxProcessings();

	/**
	 * @param maxProcessings maximum number of processings in the statistics
	 */
	void setMaxProcessings(int maxProcessings);

}
