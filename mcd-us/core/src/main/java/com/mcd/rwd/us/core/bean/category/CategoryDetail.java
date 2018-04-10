package com.mcd.rwd.us.core.bean.category;

import java.util.List;

/**
 * Created by deepti_b on 3/10/2016.
 */
public class CategoryDetail {

    private String id;

    private String name;

    private String externalId;

    private String description;

    private String marketingName;

    private String shortDescription;

    private String imagePath;

    private String heroTextAlignment;

    private List<CategoryItemInfo> items;

    public List<CategoryItemInfo> getItems() {
        return items;
    }

    public void setItems(List<CategoryItemInfo> item) {
        this.items = item;
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

    public void setName(String categoryName) {
        this.name = categoryName;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String categoryDescription) {
        this.description = categoryDescription;
    }

    public String getMarketingName() {
        return marketingName;
    }

    public void setMarketingName(String categoryMarketingName) {
        this.marketingName = categoryMarketingName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String categoryHeroImagePath) {
        this.imagePath = categoryHeroImagePath;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String categoryShortDescription) {
        this.shortDescription = categoryShortDescription;
    }

    public String getHeroTextAlignment() {
        return heroTextAlignment;
    }

    public void setHeroTextAlignment(String heroTextAlignment) {
        this.heroTextAlignment = heroTextAlignment;
    }


}
