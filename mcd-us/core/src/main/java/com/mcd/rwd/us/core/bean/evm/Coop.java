package com.mcd.rwd.us.core.bean.evm;

import com.google.gson.annotations.SerializedName;

public class Coop {
	
	@SerializedName(value = "coop_id")
	private int coopId;

	private String name;

	public int getCoopId() {
		return coopId;
	}

	public void setCoopId(int coopId) {
		this.coopId = coopId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
