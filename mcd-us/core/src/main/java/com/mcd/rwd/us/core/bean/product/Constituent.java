package com.mcd.rwd.us.core.bean.product;

import com.google.gson.annotations.SerializedName;

public class Constituent {
	
	@SerializedName(value = "product_marketing_name")
	private Object productMarketingName;

	@SerializedName(value = "product_name")
	private Object productName;
	
	@SerializedName(value = "is_default")
	private boolean isDefault;
	
	@SerializedName(value = "attach_product_thumbnail_image")
    private HeroImage attachComponentImage;

	public String getProductMarketingName() {
		if (productMarketingName instanceof String) {
			return (String) productMarketingName;
		}
		return null;
	}

	public void setProductMarketingName(Object productMarketingName) {
		this.productMarketingName = productMarketingName;
	}

	public String getProductName() {
		if (productName instanceof String) {
			return (String) productName;
		}
		return null;
	}

	public void setProductName(Object productName) {
		this.productName = productName;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public HeroImage getAttachComponentImage() {
		return attachComponentImage;
	}

	public void setAttachComponentImage(HeroImage attachComponentImage) {
		this.attachComponentImage = attachComponentImage;
	}

}
