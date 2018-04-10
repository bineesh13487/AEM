


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
public class ConstituentAdapter implements JsonDeserializer<List<Constituent>> {
    /**
     * default logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConstituentAdapter.class);

    @Override
    public List<Constituent> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        List<Constituent> itemConstituentList = new ArrayList<Constituent>();
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                itemConstituentList.add((Constituent) ctx.deserialize(e, Constituent.class));
            }
        } else if (json.isJsonObject()) {
            itemConstituentList.add((Constituent) ctx.deserialize(json, Constituent.class));
        } else {
            LOGGER.info("Unexpected JSON type");
        }
        return itemConstituentList;
    }
}