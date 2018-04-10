package com.mcd.rwd.us.core.bean.search;

/**
 * Created by Seema Pandey on 26-05-2016.
 */
public final class Hit {

	/**
	 * The title of the Result Hit.
	 */
	private String title;


	/**
	 * The titleForAlt of the Result Hit.
	 */
	private String titleForAlt;

	/**
	 * The description of the Result Hit.
	 */
	private String description;

	private String descriptionAria;

	/**
	 * The date of the Result Hit.
	 */
	private String date;

	/**
	 * The URL of the Result Hit.
	 */
	private String url;
	
	private String productNutrition;
	
	private String thumbnail;

	private String defaultImagePath;

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

	/**
	 * Getter for Tile.
	 * @return title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the Title.
	 * @param title
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	public String getTitleForAlt() { return titleForAlt; }

	public void setTitleForAlt(String titleForAlt) { this.titleForAlt = titleForAlt; }

	/**
	 * Getter for Description.
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for Description.
	 * @param description
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Getter for Date.
	 * @return date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Setter for Date.
	 * @param date
	 */
	public void setDate(final String date) {
		this.date = date;
	}

	/**
	 * Getter for URL.
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Setter for URL.
	 * @param url
	 */
	public void setUrl(final String url) {
		this.url = url;
	}
	
	public String getDescriptionAria() {
		return descriptionAria;
	}

	public void setDescriptionAria(String descriptionAria) {
		this.descriptionAria = descriptionAria;
	}
}
