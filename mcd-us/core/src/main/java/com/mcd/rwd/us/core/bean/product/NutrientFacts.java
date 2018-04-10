package com.mcd.rwd.us.core.bean.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by deepti_b on 3/21/2016.
 */
public class NutrientFacts {
    private List<Nutrient> nutrient;

    public List<Nutrient> getNutrient() {
        return this.nutrient;
    }


    public void setNutrient(List<Nutrient> nutrientList) {
        this.nutrient = nutrientList;
        if (nutrient != null && !nutrient.isEmpty()) {
            Collections.sort(nutrient, new Comparator<Nutrient>() {
                @Override
                public int compare(Nutrient o1, Nutrient o2) {
                    return o1.getId() - o2.getId();
                }
            });
        }
    }

    public List<Nutrient> getNutrientsList() {
        if (nutrient != null && !nutrient.isEmpty()) {
            return nutrient;
        } else {
            return new ArrayList<Nutrient>();
        }
    }
}
