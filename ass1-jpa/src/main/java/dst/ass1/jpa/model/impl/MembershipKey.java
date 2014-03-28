package dst.ass1.jpa.model.impl;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.IPerson;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.IWorkPlatform;

@Embeddable
public class MembershipKey implements IMembershipKey, Serializable {
	
	private static final long serialVersionUID = 5336920996779969887L;
	private Long userId;
	private Long workPlatformId;
	
	@Transient private IUser user;
	@Transient private IWorkPlatform workPlatform;

	@Override
	public IUser getUser() {
		return this.user;
	}

	@Override
	public void setUser(IUser user) {
		this.user = user;
		this.userId = ((IPerson) user).getId();
	}

	@Override
	public IWorkPlatform getWorkPlatform() {
		return this.workPlatform;
	}

	@Override
	public void setWorkPlatform(IWorkPlatform platform) {
		this.workPlatform = platform;
		this.workPlatformId = platform.getId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result	+ ((workPlatformId == null) ? 0 : workPlatformId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		MembershipKey other = (MembershipKey) obj;
		if (userId == null) {
			if (other.userId != null) return false;
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		
		if (workPlatformId == null) {
			if (other.workPlatformId != null) return false;
		} else if (!workPlatformId.equals(other.workPlatformId)) {
			return false;
		}
		return true;
	}

}
