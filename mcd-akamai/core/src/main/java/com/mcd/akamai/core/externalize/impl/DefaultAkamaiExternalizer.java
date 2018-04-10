package com.mcd.akamai.core.externalize.impl;

import com.adobe.acs.commons.util.ParameterUtil;
import com.google.common.collect.Sets;
import com.mcd.akamai.core.config.AkamaiDomainConfiguration;
import com.mcd.akamai.core.externalize.AkamaiExternalizer;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
@Component(immediate = true, metatype = true, label = "MCD Akamai Externalizer", description = "Maps internal URLs to" +
        " the externalized equivalents, used when flushing entries from the Akamai cache")
public class DefaultAkamaiExternalizer implements AkamaiExternalizer {

    @Property(label = "Path Mappings", unbounded = PropertyUnbounded.ARRAY, value = {""}, description = "Mappings " +
            "between an internal path and the domain used by akamai for content under that path.  Internal paths and " +
            "Akamai domains should be separated by a '=' character, and domains should not end with '/'. Example: " +
            "\"/content/us/en-us=https://www.mcdonalds.com/en/en-us\"")
    private static final String PATH_MAPPINGS = "path.mappings";

    private static final String SEPARATOR = "=";

    private final Map<String, String> mappings = new LinkedHashMap<>();

    @Reference
    private AkamaiDomainConfiguration domainConfiguration;

    @Override
    public Set<String> mapPath(String internal) {
        if (internal != null) {
            for (Map.Entry<String, String> entry : mappings.entrySet()) {
                if (internal.startsWith(entry.getKey())) {
                    return Sets.newHashSet(entry.getValue() + internal.substring(entry.getKey().length()));
                }
            }
        }
        // The path is not mapped to particular domain -- treat as an asset, clearing all possible domains
        final Set<String> out = Sets.newHashSet();
        domainConfiguration.getAssetDomains().forEach(domain -> out.add(domain + internal));
        return out;
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
