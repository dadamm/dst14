package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IAddress;
import dst.ass1.jpa.model.IPerson;

public abstract class AbstractPerson implements IPerson {
	
	private Long id;
	private String lastName;
	private String firstName;
	private IAddress address;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getLastName() {
		return this.lastName;
	}

	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String getFirstName() {
		return this.firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public IAddress getAddress() {
		return this.address;
	}

	@Override
	public void setAddress(IAddress address) {
		this.address = address;
	}

}
