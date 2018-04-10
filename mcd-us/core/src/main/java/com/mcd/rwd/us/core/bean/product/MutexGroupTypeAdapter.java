

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
public class MutexGroupTypeAdapter implements JsonDeserializer<List<MutexGroup>> {
    /**
     * default logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MutexGroupTypeAdapter.class);

    @Override
    public List<MutexGroup> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        List<MutexGroup> itemMutexGroupList = new ArrayList<MutexGroup>();
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                itemMutexGroupList.add((MutexGroup) ctx.deserialize(e, MutexGroup.class));
            }
        } else if (json.isJsonObject()) {
            itemMutexGroupList.add((MutexGroup) ctx.deserialize(json, MutexGroup.class));
        } else {
            LOGGER.info("Unexpected JSON type");
        }
        return itemMutexGroupList;
    }
}