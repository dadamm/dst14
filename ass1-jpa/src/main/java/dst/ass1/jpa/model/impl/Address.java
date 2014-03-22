package dst.ass1.jpa.model.impl;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import dst.ass1.jpa.model.IAddress;

@Embeddable
public class Address implements IAddress {
	
	@Column(name = "street", length = 50)
	private String street;
	
	@Column(name = "city", length = 50)
	private String city;
	
	@Column(name = "zipcode", length = 10)
	private String zipCode;

	@Override
	public String getStreet() {
		return this.street;
	}

	@Override
	public void setStreet(String street) {
		this.street = street;
	}

	@Override
	public String getCity() {
		return this.city;
	}

	@Override
	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String getZipCode() {
		return this.zipCode;
	}

	@Override
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}
