package dst.ass1.jpa.model.impl;

import java.util.Date;

import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;

public class Membership implements IMembership {
	
	private IMembershipKey id;
	private Date registration;
	private Double discount;

	@Override
	public IMembershipKey getId() {
		return this.id;
	}

	@Override
	public void setId(IMembershipKey id) {
		this.id = id;
	}

	@Override
	public Date getRegistration() {
		return this.registration;
	}

	@Override
	public void setRegistration(Date registration) {
		this.registration = registration;
	}

	@Override
	public Double getDiscount() {
		return this.discount;
	}

	@Override
	public void setDiscount(Double discount) {
		this.discount = discount;
	}

}
