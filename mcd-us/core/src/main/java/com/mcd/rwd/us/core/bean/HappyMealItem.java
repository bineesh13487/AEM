package com.mcd.rwd.us.core.bean;


/**
 * Created by Dipankar Gupta on 25-03-2016.
 */
public class HappyMealItem {

	private String title;

	private String imageUrl;
	
	private String imageAlt;
	
	private int itemId;
	
	private String coops;
	
	public String getCoops() {
		return coops;
	}

	public void setCoops(String coops) {
		this.coops = coops;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
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

	public String getImageAlt() {
		return imageAlt;
	}

	public void setImageAlt(String imageAlt) {
		this.imageAlt = imageAlt;
	}

	}
