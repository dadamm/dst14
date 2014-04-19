package dst.ass2.ejb.model;

import java.math.BigDecimal;

public interface IPrice {

	public Long getId();

	public void setId(Long id);

	public Integer getNrOfHistoricalTasks();

	public void setNrOfHistoricalTasks(Integer nrOfHistoricalTasks);

	public BigDecimal getPrice();

	public void setPrice(BigDecimal price);

}