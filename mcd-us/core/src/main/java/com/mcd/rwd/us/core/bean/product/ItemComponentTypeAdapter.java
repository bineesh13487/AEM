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
 * Created by deepti_b on 6/7/2016.
 */
public class ItemComponentTypeAdapter implements JsonDeserializer<List<ItemComponent>> {
    /**
     * default logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemComponentTypeAdapter.class);

    @Override
    public List<ItemComponent> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        List<ItemComponent> itemComponentList = new ArrayList<ItemComponent>();
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                itemComponentList.add((ItemComponent) ctx.deserialize(e, ItemComponent.class));
            }
        } else if (json.isJsonObject()) {
            itemComponentList.add((ItemComponent) ctx.deserialize(json, ItemComponent.class));
        } else {
            LOGGER.info("Unexpected JSON type");
        }
        return itemComponentList;
    }
}