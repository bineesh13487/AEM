package com.mcd.rwd.global.core.replication.invalidators.impl;

import com.mcd.rwd.global.core.replication.invalidators.Invalidator;
import org.apache.commons.lang.ArrayUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingConstants;
import org.osgi.service.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * DesignContentInvalidatorImpl
 *  Looks for changes under /etc/designs and will send paths on for cache flushing
 */
@Component(
        label = "Design Content Invalidator",
        description = "looks for changes to /etc/design changes and forwards paths to a dispatcher flusher",
        configurationFactory = true)
@Properties({
        @Property(
                label = "Service Name",
                name = Invalidator.PROP_NAME,
                description = "uniquely identify the service impl by name.",
                value = DesignContentInvalidatorImpl.SERVICE_NAME
        )
})
@Service
public class DesignContentInvalidatorImpl implements Invalidator {

    public static final String SERVICE_NAME = "designContentInvalidator";
    private static final Logger LOG = LoggerFactory.getLogger(DesignContentInvalidatorImpl.class);

    @Override
    public List<String> processResourceEvent(final Event event) {
        final String path = event.getProperty(SlingConstants.PROPERTY_PATH).toString();
        List<String> flushPaths = new ArrayList<>();

        if (path.startsWith(ROOT_DESIGN_PATH)) {
            LOG.debug("Clearing dispatcher with detected resource change on path " + path);
            final Matcher matcher = ROOT_DESIGN_SITE_PATTERN.matcher(path);
            if (matcher.find()) {
                final String country = matcher.group(GROUP_COUNTRY);
                final String language = matcher.group(GROUP_LANGUAGE);
                flushPaths.add(String.format(SITE_TEMPLATE, country, language));
                flushPaths.add(String.format(DESIGN_TEMPLATE, country, language));
            }
            LOG.info("sending paths to flush: " + ArrayUtils.toString(flushPaths.toArray()));
        }

        return flushPaths;
    }

    @Override
    public String getInvalidatorServiceId() {
        return SERVICE_NAME;
    }

}
