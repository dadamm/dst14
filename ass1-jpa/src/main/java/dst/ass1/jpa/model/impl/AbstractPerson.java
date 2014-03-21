package dst.ass1.jpa.model.impl;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import dst.ass1.jpa.model.IAddress;
import dst.ass1.jpa.model.IPerson;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "person")
public abstract class AbstractPerson implements IPerson {
	
	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "lastname")
	private String lastName;
	
	@Column(name = "firstname")
	private String firstName;
	
	// TODO is not possible to use interface
	@Embedded
	private Address address;

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
		if(address instanceof Address) {
			this.address = (Address) address;
		} else throw new IllegalArgumentException("The argument is not a type of Address");
	}

}
