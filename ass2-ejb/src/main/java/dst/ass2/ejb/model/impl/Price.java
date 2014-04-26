package dst.ass2.ejb.model.impl;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import dst.ass2.ejb.model.IPrice;

@Entity
public class Price implements IPrice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "nrofhistoricaltasks")
	private Integer nrOfHistoricalTasks;
	
	@Column(name = "price")
	private BigDecimal price;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public BigDecimal getPrice() {
		return price;
	}

	@Override
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public Integer getNrOfHistoricalTasks() {
		return this.nrOfHistoricalTasks;
	}

	@Override
	public void setNrOfHistoricalTasks(Integer nrOfHistoricalTasks) {
		this.nrOfHistoricalTasks = nrOfHistoricalTasks;
	}

}
