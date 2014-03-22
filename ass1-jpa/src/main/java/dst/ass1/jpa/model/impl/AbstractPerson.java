package dst.ass1.jpa.model.impl;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import dst.ass1.jpa.model.IAddress;
import dst.ass1.jpa.model.IPerson;
import dst.ass1.jpa.util.Constants;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = Constants.T_PERSON)
public abstract class AbstractPerson implements IPerson {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "lastname", length = 50)
	private String lastName;
	
	@Column(name = "firstname", length = 50)
	private String firstName;
	
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
