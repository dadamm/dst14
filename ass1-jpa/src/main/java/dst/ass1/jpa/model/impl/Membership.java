package dst.ass1.jpa.model.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.IWorkPlatform;
import dst.ass1.jpa.util.Constants;

@Entity
@Table(name = Constants.T_MEMBERSHIP)
public class Membership implements IMembership {
	
	@EmbeddedId
	private MembershipKey id;
	
	@Column(name = "registration")
	private Date registration;
	
	@Column(name = "discount")
	private Double discount;
	
	@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "userId")
	@ManyToOne(targetEntity = User.class)
	private IUser user;

	@PrimaryKeyJoinColumn(name = "workplatform_id",referencedColumnName = "workPlatform")
	@ManyToOne(targetEntity = WorkPlatform.class)
	private IWorkPlatform workPlatform;

	@Override
	public IMembershipKey getId() {
		return this.id;
	}

	@Override
	public void setId(IMembershipKey id) {
		if(id instanceof MembershipKey) {
			this.id = (MembershipKey) id;
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
