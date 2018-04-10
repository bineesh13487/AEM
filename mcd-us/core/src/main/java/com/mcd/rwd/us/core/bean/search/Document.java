package com.mcd.rwd.us.core.bean.search;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Seema Pandey on 26-05-2016.
 */
public final class Document {

	private String id;

	private String description;

	private String url;

	private String keywords;

	private String title;

	@SerializedName("tstamp")
	private String timestamp;
	
	@SerializedName("productnutrition")
	private String productNutrition;

	@SerializedName("thumbnail")
	private String thumbnail;
	
	@SerializedName("defaultimagepath")
	private String defaultImagePath;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getProductNutrition() {
		return productNutrition;
	}

	public void setProductNutrition(String productNutrition) {
		this.productNutrition = productNutrition;
	}


	public String getThumbnail() { return thumbnail; }

	public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

	public String getDefaultImagePath() {
		return defaultImagePath;
	}

	public void setDefaultImagePath(String defaultImagePath) {
		this.defaultImagePath = defaultImagePath;
	}
}
