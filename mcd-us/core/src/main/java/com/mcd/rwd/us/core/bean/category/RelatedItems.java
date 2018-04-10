package com.mcd.rwd.us.core.bean.category;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by deepti_b on 3/31/2016.
 */
public class RelatedItems {

    @SerializedName(value = "related_item")
    private List<RelatedItem> relatedItem;

    public List<RelatedItem> getRelatedItem() {
        return relatedItem;
    }

    public void setRelatedItem(List<RelatedItem> relatedItem) {
        this.relatedItem = relatedItem;
    }
}

