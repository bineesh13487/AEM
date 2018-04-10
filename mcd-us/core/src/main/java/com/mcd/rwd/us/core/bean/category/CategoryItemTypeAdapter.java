package com.mcd.rwd.us.core.bean.category;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepti_b on 5/31/2016.
 */
public class CategoryItemTypeAdapter implements JsonDeserializer<CategoryItem[]> {
    /**
     * default logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryItemTypeAdapter.class);

    @Override
    public CategoryItem[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        List<CategoryItem> categoryItemList = new ArrayList<CategoryItem>();
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                categoryItemList.add((CategoryItem) ctx.deserialize(e, CategoryItem.class));
            }
        } else if (json.isJsonObject()) {
            categoryItemList.add((CategoryItem) ctx.deserialize(json, CategoryItem.class));
        } else {
            LOGGER.info("Unexpected JSON type");
        }
        CategoryItem[] itemArr = new CategoryItem[categoryItemList.size()];
        itemArr = categoryItemList.toArray(itemArr);
        return itemArr;
    }
}

