package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.IUser;
import dst.ass1.jpa.model.IWorkPlatform;

public class MembershipKey implements IMembershipKey {
	
	private IUser user;
	private IWorkPlatform workPlatform;

	@Override
	public IUser getUser() {
		return this.user;
	}

	@Override
	public void setUser(IUser user) {
		this.setUser(user);
	}

	@Override
	public IWorkPlatform getWorkPlatform() {
		return this.workPlatform;
	}

	@Override
	public void setWorkPlatform(IWorkPlatform platform) {
		this.setWorkPlatform(platform);
	}

}
