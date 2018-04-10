package com.mcd.rwd.us.core.bean.product;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepti_b on 3/22/2016.
 */
public class HeroImage {

    private String imagePath;

    @SerializedName(value = "image_name")
    private Object imageName;

    private Object description;

    @SerializedName(value = "alt_text")
    private Object altText;

    public String getImageName() {
        if (imageName instanceof String) {
            return (String) imageName;
        }
        return null;
    }

    public void setImageName(Object imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
}

