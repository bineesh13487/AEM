package com.mcd.rwd.us.core.bean.category;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepti_b on 3/8/2016.
 */
public class CategoryItemThumbnailImage {

    @SerializedName(value = "image_name")
    private Object imageName;

    private Object description;

    @SerializedName(value = "alt_text")
    private Object altText;

    private Object url;

    private String imageFullPath;

    public String getImageFullPath() {
        return imageFullPath;
    }

    public void setImageFullPath(String imageFullPath) {
        this.imageFullPath = imageFullPath;
    }

    public String getImageName() {
        if (imageName instanceof String) {
            return (String) imageName;
        }
        return null;
    }

    public void setImageName(Object imageName) {
        this.imageName = imageName;
    }

    public String getDescription() {
        if (description instanceof String) {
            return (String) description;
        }
        return null;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getAltText() {
        if (altText instanceof String) {
            return (String) altText;
        }
        return null;
    }

    public void setAltText(Object altText) {
        this.altText = altText;
    }

    public String getUrl() {
        if (url instanceof String) {
            return (String) url;
        }
        return null;
    }

    public void setUrl(Object url) {
        this.url = url;
    }
}


