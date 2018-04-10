package com.mcd.rwd.us.core.bean.product;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepti_b on 3/25/2016.
 */
public class NutrientTypeAdapter implements JsonDeserializer<List<Nutrient>> {
    /**
     * default logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NutrientTypeAdapter.class);

    @Override
    public List<Nutrient> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        List<Nutrient> nutrientItemList = new ArrayList<Nutrient>();
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                nutrientItemList.add((Nutrient) ctx.deserialize(e, Nutrient.class));
            }
        } else if (json.isJsonObject()) {
            nutrientItemList.add((Nutrient) ctx.deserialize(json, Nutrient.class));
        } else {
            LOGGER.info("Unexpected JSON type");
        }
        return nutrientItemList;
    }
}