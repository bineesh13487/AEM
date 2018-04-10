package com.mcd.rwd.us.core.bean.product;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MutexGroups {
	
	@SerializedName(value = "mutex_group")
	private List<MutexGroup> mutexGroup;

	public List<MutexGroup> getMutexGroup() {
		return mutexGroup;
	}

	public void setMutexGroup(List<MutexGroup> mutexGroup) {
		this.mutexGroup = mutexGroup;
	}

}
