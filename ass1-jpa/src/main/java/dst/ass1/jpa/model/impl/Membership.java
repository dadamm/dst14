package dst.ass1.jpa.model.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.util.Constants;

@Entity
@Table(name = Constants.T_MEMBERSHIP)
@IdClass(MembershipKey.class)
public class Membership implements IMembership {
	
	@Id
	@ManyToOne(targetEntity = User.class, optional = false)
	private IUser user;
	
	@Id
	@ManyToOne(targetEntity = WorkPlatform.class, optional = false)
	private IWorkPlatform workPlatform;
	
	@Transient
	private MembershipKey id;
	
	@Column(name = "registration")
	private Date registration;
	
	@Column(name = "discount")
	private Double discount;
	

	@Override
	public IMembershipKey getId() {
		return this.id;
	}

	@Override
	public void setId(IMembershipKey id) {
		if(id instanceof MembershipKey) {
			this.id = (MembershipKey) id;
			this.user = id.getUser();
			this.workPlatform = id.getWorkPlatform();
		} else throw new IllegalArgumentException("The argument is not a type of MembershipKey");
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
