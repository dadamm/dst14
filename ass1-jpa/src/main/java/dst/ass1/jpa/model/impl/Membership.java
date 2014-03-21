package dst.ass1.jpa.model.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.IWorkPlatform;

@Entity
public class Membership implements IMembership {
	
	// TODO use interface
	@EmbeddedId
	private MembershipKey id;
	
	@Column(name = "registration")
	private Date registration;
	
	@Column(name = "discount")
	private Double discount;
	
	@MapsId("userId")
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private IUser user;

	@MapsId("workPlatformId")
	@ManyToOne(targetEntity = WorkPlatform.class)
	@JoinColumn(name = "workplatform_id")
	private IWorkPlatform workPlatform;

	@Override
	public IMembershipKey getId() {
		return this.id;
	}

	// TODO change when interface is used
	@Override
	public void setId(IMembershipKey id) {
		this.id = (MembershipKey) id;
//		this.id = id;
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
