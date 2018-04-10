package com.mcd.rwd.us.core.bean.category;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by deepti_b on 3/31/2016.
 */
public class RelationTypes {

    @SerializedName(value = "relation_type")
    private List<RelationType> relationType;

    public List<RelationType> getRelationType() {
        return relationType;
    }

    public void setRelationType(List<RelationType> relationType) {
        this.relationType = relationType;
    }

}