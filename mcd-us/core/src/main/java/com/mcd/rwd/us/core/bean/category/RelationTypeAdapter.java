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
public class RelationTypeAdapter implements JsonDeserializer<List<RelationType>> {
    /**
     * default logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RelationTypeAdapter.class);

    @Override
    public List<RelationType> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        List<RelationType> relationTypeList = new ArrayList<RelationType>();
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                relationTypeList.add((RelationType) ctx.deserialize(e, RelationType.class));
            }
        } else if (json.isJsonObject()) {
            relationTypeList.add((RelationType) ctx.deserialize(json, RelationType.class));
        } else {
            LOGGER.info("Unexpected JSON type");
        }
        return relationTypeList;
    }
}

