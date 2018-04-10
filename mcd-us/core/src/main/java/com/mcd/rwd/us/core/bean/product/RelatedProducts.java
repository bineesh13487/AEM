package com.mcd.rwd.us.core.bean.product;

/**
 * Created by deepti_b on 4/4/2016.
 */
public class RelatedProducts {

    private int categoryID;

    private String sectionTitle;
    
    private String ariaPrev;
    
    private String ariaNext;
    
    private String ariaSlider;

    private String linkTitle;

    private String linkUrl;

    private String itemsCount;

    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String marketingName) {
        this.categoryName = categoryName;
    }

    public String getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(String itemsCount) {
        this.itemsCount = itemsCount;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public int  getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }
    
    public String getAriaPrev() {
        return ariaPrev;
    }

    public void setAriaPrev(String ariaPrev) {
        this.ariaPrev = ariaPrev;
    }
    
    public String getAriaNext() {
        return ariaNext;
    }

    public void setAriaNext(String ariaNext) {
        this.ariaNext = ariaNext;
    
    }
    public String getAriaSlider() {
        return ariaSlider;
    }

    public void setAriaSlider(String ariaSlider) {
        this.ariaSlider = ariaSlider;
    
    }

}

