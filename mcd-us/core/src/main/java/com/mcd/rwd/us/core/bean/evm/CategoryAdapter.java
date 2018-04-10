package com.mcd.rwd.us.core.bean.evm;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.mcd.rwd.us.core.bean.category.Category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;


/**
 * Created by deepti_b on 3/31/2016.
 */
public class CategoryAdapter implements JsonDeserializer<List<Category>> {
    /**
     * default logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryAdapter.class);

    @Override
    public List<Category> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        List<Category> categoryTypeList = new ArrayList<Category>();
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                categoryTypeList.add((Category) ctx.deserialize(e, Category.class));
            }
        } else if (json.isJsonObject()) {
            categoryTypeList.add((Category) ctx.deserialize(json, Category.class));
        } else {
            LOGGER.info("Unexpected JSON type");
        }
        return categoryTypeList;
    }
}

