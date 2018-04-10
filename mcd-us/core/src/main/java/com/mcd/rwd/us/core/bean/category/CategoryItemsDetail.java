package com.mcd.rwd.us.core.bean.category;

import com.google.gson.annotations.SerializedName;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

/**
 * Created by deepti_b on 3/9/2016.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CategoryItemsDetail {

    private Object id;

    @SerializedName(value = "category_name")
    private Object categoryName;

    @SerializedName(value = "category_id")
    private Object categoryId;

    @SerializedName(value = "external_id")
    private Object externalId;

    @SerializedName(value = "category_description")
    private Object categoryDescription;

    @SerializedName(value = "category_marketing_name")
    private Object categoryMarketingName;

    private String categoryShortDescription;

    private String categoryHeroImagePath;

    private String categoryMobileImagePath;

    private String heroTextAlignment;

    private CategoryItems items;

    public CategoryItems getItems() {
        return items;
    }

    public void setItems(CategoryItems items) {
        this.items = items;
    }

    public String getCategoryMobileImagePath() {
        return categoryMobileImagePath;
    }

    public void setCategoryMobileImagePath(String categoryMobileImagePath) {
        this.categoryMobileImagePath = categoryMobileImagePath;
    }

    public int getId() {
        if (id instanceof Double) {
            return ((Double) id).intValue();
        }
        return ApplicationConstants.DEFAULT_INT;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getCategoryName() {
        if (categoryName instanceof String) {
            return (String) categoryName;
        }
        return null;
    }

    public void setCategoryName(Object categoryName) {
        this.categoryName = categoryName;
    }

    public int getExternalId() {
        if (externalId instanceof Double) {
            return ((Double) externalId).intValue();
        }
        return ApplicationConstants.DEFAULT_INT;
    }

    public void setExternalId(Object externalId) {
        this.externalId = externalId;
    }

    public int getCategoryId() {
        if (categoryId instanceof Double) {
            return ((Double) categoryId).intValue();
        }
        if (categoryId instanceof Integer) {
            return ((Integer) categoryId).intValue();
        }
        return ApplicationConstants.DEFAULT_INT;
    }

    public void setCategoryId(Object categoryId) {
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

    public String getCategoryShortDescription() {
        return categoryShortDescription;
    }

    public void setCategoryShortDescription(String categoryShortDescription) {
        this.categoryShortDescription = categoryShortDescription;
    }

    public String getCategoryHeroImagePath() {
        return categoryHeroImagePath;
    }

    public void setCategoryHeroImagePath(String categoryHeroImagePath) {
        this.categoryHeroImagePath = categoryHeroImagePath;
    }

    public String getHeroTextAlignment() {
        return heroTextAlignment;
    }

    public void setHeroTextAlignment(String heroTextAlignment) {
        this.heroTextAlignment = heroTextAlignment;
    }

}
