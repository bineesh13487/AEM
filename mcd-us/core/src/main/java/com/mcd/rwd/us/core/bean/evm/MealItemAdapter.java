package com.mcd.rwd.us.core.bean.evm;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;


/**
 * Created by deepti_b on 3/31/2016.
 */
public class MealItemAdapter implements JsonDeserializer<List<MealItem>> {
    /**
     * default logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MealItemAdapter.class);

    @Override
    public List<MealItem> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        List<MealItem> itemTypeList = new ArrayList<MealItem>();
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                itemTypeList.add((MealItem) ctx.deserialize(e, MealItem.class));
            }
        } else if (json.isJsonObject()) {
            itemTypeList.add((MealItem) ctx.deserialize(json, MealItem.class));
        } else {
            LOGGER.info("Unexpected JSON type");
        }
        return itemTypeList;
    }
}

