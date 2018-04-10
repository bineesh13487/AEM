package com.mcd.rwd.us.core.bean.category;

/**
 * Created by deepti_b on 3/10/2016.
 */
public class CategoryItemInfo {

    private String marketingName;

    private String externalId;

    private String id;

    private String name;

    private String displayOrder;

    private String doNotShow;

    private String imagePath;

    private String shortName;

    private String path;

    private String description;

    private boolean itemVisibility = true;
    
    private String specializationText1;
    
    private String specializationText2;
    
	public String getPath() {
        return path;
    }

    public void setPath(String categoryItemFullPath) {
        this.path = categoryItemFullPath;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String itemName) {
        this.name = itemName;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getDoNotShow() {
        return doNotShow;
    }

    public void setDoNotShow(String doNotShow) {
        this.doNotShow = doNotShow;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imageFullPath) {
        this.imagePath = imageFullPath;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getMarketingName() {
        return marketingName;
    }

    public void setMarketingName(String itemMarketingName) {
        this.marketingName = itemMarketingName;
    }

    public boolean isItemVisibility() {
        return itemVisibility;
    }

    public void setItemVisibility(boolean itemVisibility) {
        this.itemVisibility = itemVisibility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSpecializationText1() {
		return specializationText1;
	}

	public void setSpecializationText1(String specializationText1) {
		this.specializationText1 = specializationText1;
	}

	public String getSpecializationText2() {
		return specializationText2;
	}

	public void setSpecializationText2(String specializationText2) {
		this.specializationText2 = specializationText2;
	}

}
