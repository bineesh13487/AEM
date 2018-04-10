package com.mcd.rwd.us.core.bean;


/**
 * Created by Dipankar Gupta on 25-03-2016.
 */
public class ExtraValueMealItem {

	private String title;

	private String imageUrl;
	
	private String description;
	
	private String imageAlt;
	
	private String itemIds;
	
	private String coopIds;

	public String getCoopIds() {
		return coopIds;
	}

	public void setCoopIds(String coopIds) {
		this.coopIds = coopIds;
	}

	public String getItemIds() {
		return itemIds;
	}

	public void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}

	public String getImageAlt() {
		return imageAlt;
	}

	public void setImageAlt(String imageAlt) {
		this.imageAlt = imageAlt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
