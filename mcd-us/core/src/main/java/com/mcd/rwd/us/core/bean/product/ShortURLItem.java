package com.mcd.rwd.us.core.bean.product;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rakesh.Balaiah on 23-05-2016.
 */
public class ShortURLItem {

	private int id;

	@SerializedName(value = "item_name") private String itemName;

	@SerializedName(value = "short_name") private Object shortName;

	@SerializedName(value = "menu_item_no") private Object menuItemNo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getShortName() {
		if(shortName instanceof String){
		return (String)shortName;}
		return null;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getMenuItemNo() {
		if (menuItemNo instanceof Double) {
			return String.valueOf(((Double) menuItemNo).intValue());
		} else if (menuItemNo instanceof String) {
			return (String) menuItemNo;
		}
		return null;
	}

	public void setMenuItemNo(Object menuItemNo) {
		this.menuItemNo = menuItemNo;
	}
}