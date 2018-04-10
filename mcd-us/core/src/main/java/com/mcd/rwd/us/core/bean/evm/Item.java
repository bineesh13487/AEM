package com.mcd.rwd.us.core.bean.evm;

import com.google.gson.annotations.SerializedName;

public class Item {
	
	@SerializedName(value = "item_id")
	private int itemId;
	
	@SerializedName(value = "do_not_show")
    private Object doNotShow;
	
	public String getDoNotShow() {
		if (doNotShow instanceof String) {
			return (String) doNotShow;
	    }
	    return null;
	}

	public void setDoNotShow(Object doNotShow) {
		this.doNotShow = doNotShow;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

}
