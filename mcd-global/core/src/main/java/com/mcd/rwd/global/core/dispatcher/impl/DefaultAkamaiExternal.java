package com.mcd.rwd.global.core.dispatcher.impl;

import com.adobe.acs.commons.util.ParameterUtil;
import com.mcd.rwd.global.core.dispatcher.AkamaiExternalizer;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Component(immediate = true, metatype = true, label = "Akamai Externalizer", description = "Maps internal URLs to " +
        "the externalized equivalents, used when flushing entries from the Akamai cache")
public class DefaultAkamaiExternal implements AkamaiExternalizer {

    @Property(label = "Path Mappings", unbounded = PropertyUnbounded.ARRAY, value = {""}, description = "Mappings " +
            "between an internal path and the domain used by akamai for content under that path.  Internal paths and " +
            "Akamai domains should be separated by a '=' character, and domains should not end with '/'. Example: " +
            "\"/content/us/en-us=https://www.mcdonalds.com/en/en-us\"")
    private static final String PATH_MAPPINGS = "path.mappings";

    private static final String SEPARATOR = "=";

    private final Map<String, String> mappings = new LinkedHashMap<>();

    @Override
    public String mapPath(String internal) {
        if (internal != null) {
            for (Map.Entry<String, String> entry : mappings.entrySet()) {
                if (internal.startsWith(entry.getKey())) {
                    return entry.getValue() + internal.substring(entry.getKey().length());
                }
            }
        }
        return internal;
    }

    @Activate
    @Modified
    protected final void activate(final Map<String, Object> props) {
        mappings.clear();
        final String[] values = PropertiesUtil.toStringArray(props.get(PATH_MAPPINGS), new String[]{});
        final Map<String, String> valueMap = ParameterUtil.toMap(values, SEPARATOR, false, "");
        for (Map.Entry<String, String> entry : valueMap.entrySet()) {
            final String path = entry.getKey().trim();
            final String domain = entry.getValue().trim();
            if (!domain.isEmpty()) {
                mappings.put(path, domain);
            }
        }
    }
}
