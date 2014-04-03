package dst.ass2.ejb.management.interfaces;

import java.math.BigDecimal;

public interface IPriceManagementBean {

	/**
	 * @param nrOfHistoricalTasks
	 * @return the price for the given number of historical jobs. If there was
	 *         no price for this number of jobs specified it returns 0.
	 */
	public BigDecimal getPrice(Integer nrOfHistoricalTasks);

	/**
	 * Creates a price-step for the given number of historical jobs.
	 * 
	 * @param nrOfHistoricalTasks
	 * @param price
	 */
	public void setPrice(Integer nrOfHistoricalTasks, BigDecimal price);

	/**
	 * Clears the cached price-steps.
	 */
	public void clearCache();

}
