package com.mcd.rwd.us.core.bean.product;

import com.google.gson.annotations.SerializedName;

public class MutexGroup {
	
	private Constituents constituents;
	
	@SerializedName(value = "display_order")
	private int displayOrder;

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Constituents getConstituents() {
		return constituents;
	}

	public void setConstituents(Constituents constituents) {
		this.constituents = constituents;
	}

}
