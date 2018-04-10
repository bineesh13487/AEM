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
 * Created by deepti_b on 3/31/2016.
 */
public class RelatedItemTypeAdapter implements JsonDeserializer<List<RelatedItem>> {
    /**
     * default logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RelatedItemTypeAdapter.class);

    @Override
    public List<RelatedItem> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        List<RelatedItem> relatedItemList = new ArrayList<RelatedItem>();
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                relatedItemList.add((RelatedItem) ctx.deserialize(e, RelatedItem.class));
            }
        } else if (json.isJsonObject()) {
            relatedItemList.add((RelatedItem) ctx.deserialize(json, RelatedItem.class));
        } else {
            LOGGER.info("Unexpected JSON type");
        }
        return relatedItemList;
    }
}
