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
public class ItemAdapter implements JsonDeserializer<List<Item>> {
    /**
     * default logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemAdapter.class);

    @Override
    public List<Item> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        List<Item> itemTypeList = new ArrayList<Item>();
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                itemTypeList.add((Item) ctx.deserialize(e, Item.class));
            }
        } else if (json.isJsonObject()) {
            itemTypeList.add((Item) ctx.deserialize(json, Item.class));
        } else {
            LOGGER.info("Unexpected JSON type");
        }
        return itemTypeList;
    }
}

