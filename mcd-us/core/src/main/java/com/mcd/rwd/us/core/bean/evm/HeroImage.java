package com.mcd.rwd.us.core.bean.evm;

import com.google.gson.annotations.SerializedName;

public class HeroImage {

	@SerializedName(value = "alt_text")
    private Object altText;
	
	private Object description;
	
	@SerializedName(value = "image_name")
	private Object imageName;
	
	private Object url;

	public String getAltText() {
		if (altText instanceof String) {
			return (String)altText;
		}
		return null;
	}

	public void setAltText(String altText) {
		this.altText = altText;
	}

	public String getDescription() {
		if (description instanceof String) {
			return (String)description;
		}
		return null;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageName() {
		if (imageName instanceof String) {
			return (String)imageName;
		}
		return null;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getUrl() {
		if (url instanceof String) {
			return (String)url;
		}
		return null;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
