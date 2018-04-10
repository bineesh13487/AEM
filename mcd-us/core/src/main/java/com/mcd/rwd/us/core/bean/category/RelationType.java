package com.mcd.rwd.us.core.bean.category;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepti_b on 3/31/2016.
 */
public class RelationType {

    @SerializedName(value = "related_items")
    private RelatedItems relatedItems;

    private Object type;

    public RelatedItems getRelatedItems() {
        return relatedItems;
    }

    public void setRelatedItems(RelatedItems relatedItems) {
        this.relatedItems = relatedItems;
    }

    public String getType() {
        if (type instanceof String) {
            return (String) type;
        }
        return null;
    }

    public void setType(Object type) {
        this.type = type;
    }
}

