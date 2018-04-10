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
public class CoopAdapter implements JsonDeserializer<List<Coop>> {
    /**
     * default logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CoopAdapter.class);

    @Override
    public List<Coop> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        List<Coop> itemTypeList = new ArrayList<Coop>();
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                itemTypeList.add((Coop) ctx.deserialize(e, Coop.class));
            }
        } else if (json.isJsonObject()) {
            itemTypeList.add((Coop) ctx.deserialize(json, Coop.class));
        } else {
            LOGGER.info("Unexpected JSON type");
        }
        return itemTypeList;
    }
}

