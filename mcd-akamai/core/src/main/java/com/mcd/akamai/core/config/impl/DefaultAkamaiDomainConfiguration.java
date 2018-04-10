package com.mcd.akamai.core.config.impl;

import com.google.common.collect.Sets;
import com.mcd.akamai.core.config.AkamaiDomainConfiguration;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;

import java.util.*;

@Service
@Component(immediate = true, metatype = true, label = "MCD Akamai Domain Configuration")
public class DefaultAkamaiDomainConfiguration implements AkamaiDomainConfiguration {

    @Property(label = "Asset Domains", value = {"https://www.mcdonalds.com"}, description = "List all domains from " +
            "which assets will be requested.")
    private static final String PROP_DOMAINS = "domains";

    private final Set<String> domains = new HashSet<>();

    @Override
    public Set<String> getAssetDomains() {
        return Sets.newHashSet(domains);
    }

    @Activate
    @Modified
    protected final void activate(final Map<String, Object> props) {
        domains.clear();
        Collections.addAll(domains, PropertiesUtil.toStringArray(props.get(PROP_DOMAINS), new String[0]));
    }
}
