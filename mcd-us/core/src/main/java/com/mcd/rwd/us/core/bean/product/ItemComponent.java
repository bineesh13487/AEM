package com.mcd.rwd.us.core.bean.product;

import com.google.gson.annotations.SerializedName;
import java.util.*;

/**
 * Created by deepti_b on 3/22/2016.
 */
public class ItemComponent {

    @SerializedName(value = "product_marketing_name")
    private Object productMarketingName;
    
    @SerializedName(value = "product_name")
    private Object productName;

	@SerializedName(value = "attach_product_thumbnail_image")
    private HeroImage attachComponentImage;


    @SerializedName(value = "product_type_name")
    private Object productTypeName;
    
    @SerializedName(value = "display_order")
    private int displayOrder;

    public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

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

    public HeroImage getAttachComponentImage() {
        return attachComponentImage;
    }

    public void setAttachComponentImage(HeroImage attachComponentImage) {
        this.attachComponentImage = attachComponentImage;
    }

    public String getProductTypeName() {
        if (productTypeName instanceof String) {
            return (String) productTypeName;
        }
        return null;
    }

    public void setProductTypeName(Object productTypeName) {
        this.productTypeName = productTypeName;
    }
}
