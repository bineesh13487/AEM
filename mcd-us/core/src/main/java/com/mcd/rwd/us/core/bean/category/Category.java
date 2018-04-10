package com.mcd.rwd.us.core.bean.category;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepti_b on 3/7/2016.
 */
public class Category {

    @SerializedName(value = "category_id")
    private int categoryId;

    @SerializedName(value = "category_description")
    private Object categoryDescription;

    @SerializedName(value = "category_marketing_name")
    private Object categoryMarketingName;

    @SerializedName(value = "display_order")
    private int displayOrder;

    @SerializedName(value = "category_name")
    private String categoryName;

    @SerializedName(value = "category_type")
    private int categoryType;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryDescription() {
        if (categoryDescription instanceof String) {
            return (String) categoryDescription;
        }

        return null;
    }

    public void setCategoryDescription(Object categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryMarketingName() {
        if (categoryMarketingName instanceof String) {
            return (String) categoryMarketingName;
        }

        return null;
    }

    public void setCategoryMarketingName(Object categoryMarketingName) {
        this.categoryMarketingName = categoryMarketingName;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

}
