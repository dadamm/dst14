package dst.ass1.jpa.model.impl;

import java.io.Serializable;

import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.IWorkPlatform;

public class MembershipKey implements IMembershipKey, Serializable {
	
	private static final long serialVersionUID = 5336920996779969887L;
	
	private IUser user;
	private IWorkPlatform workPlatform;

	@Override
	public IUser getUser() {
		return this.user;
	}

	@Override
	public void setUser(IUser user) {
		this.user = user;
	}

	@Override
	public IWorkPlatform getWorkPlatform() {
		return this.workPlatform;
	}

	@Override
	public void setWorkPlatform(IWorkPlatform platform) {
		this.workPlatform = platform;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result	+ ((workPlatform == null) ? 0 : workPlatform.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MembershipKey other = (MembershipKey) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (workPlatform == null) {
			if (other.workPlatform != null)
				return false;
		} else if (!workPlatform.equals(other.workPlatform))
			return false;
		return true;
	}

}
