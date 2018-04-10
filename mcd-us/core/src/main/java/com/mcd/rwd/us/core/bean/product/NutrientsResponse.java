package com.mcd.rwd.us.core.bean.product;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepti_b on 3/21/2016.
 */
public class NutrientsResponse {
    @SerializedName(value = "nutrient_facts")
    private NutrientFacts nutrientFacts;

    public NutrientFacts getNutrientFacts() {
        return nutrientFacts;
    }

    public void setNutrientFacts(NutrientFacts nutrientFacts) {
        this.nutrientFacts = nutrientFacts;
    }
}
