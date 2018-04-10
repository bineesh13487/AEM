package com.mcd.rwd.global.core.replication;

import com.adobe.acs.commons.replication.dispatcher.DispatcherFlusher;
import com.day.cq.replication.*;
import com.mcd.rwd.global.core.replication.invalidators.Invalidator;
import org.apache.commons.lang.ArrayUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 *
 */
@Service
@Component(
        label = "MCD Sling Event Handler - Runs on Publish instance only",
        description = "implements a custom event listener for sling resource changes",
        policy = ConfigurationPolicy.REQUIRE,
        immediate = true
)
@Properties({
        @Property(
                name = EventConstants.EVENT_TOPIC,
                value = {SlingConstants.TOPIC_RESOURCE_ADDED,
                        SlingConstants.TOPIC_RESOURCE_CHANGED, SlingConstants.TOPIC_RESOURCE_REMOVED
                }),
        @Property(name = EventConstants.EVENT_FILTER, value = "(|(path=/content/*)(path=/etc/designs/*))"),
        @Property(name = ContentChangeListener.INVALIDATOR_PROPERTY,
                value = { "designContentInvalidator", "inheritedParsysInvalidator", "navContentInvalidator" },
                unbounded = PropertyUnbounded.ARRAY,
                label = "Invalidator services")
})
public class ContentChangeListener implements EventHandler {
    public static final String INVALIDATOR_PROPERTY = "active.invalidators";
    private static final String LOG_ENTRY_TEMPLATE = "{}: {}";
    private static final Logger LOG = LoggerFactory.getLogger(ContentChangeListener.class);
    private String[] invalidators;

    @Reference(
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
            bind = "bindInvalidatorService",
            unbind = "unbindInvalidatorService",
            referenceInterface = com.mcd.rwd.global.core.replication.invalidators.Invalidator.class,
            policy = ReferencePolicy.DYNAMIC
    )
    private ConcurrentMap<String, Invalidator> invalidatorServiceMap = new ConcurrentHashMap<>();

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private DispatcherFlusher flusher;

    @Override
    public void handleEvent(final Event event) {
        List<String> flushPaths = new ArrayList<>();

        for (String invalidator: invalidators) {
            Invalidator invalidatorService = getInvalidatorServiceById(invalidator);
            if (invalidatorService != null) {
                flushPaths.addAll(invalidatorService.processResourceEvent(event));
            } else {
                LOG.warn("Invalidator service " + invalidator + " was not found. check for typo or missing implementation");
            }
        }

        if (flushPaths.size() > 0) {
            ResourceResolver resolver = null;
            try {
                resolver = resolverFactory.getAdministrativeResourceResolver(null);
                final Map<Agent, ReplicationResult> out = flusher.flush(resolver,
                        ReplicationActionType.DELETE,
                        true, AgentFilter.DEFAULT,
                        flushPaths.toArray(new String[flushPaths.size()]));
                out.entrySet().forEach(e ->
                        LOG.info(LOG_ENTRY_TEMPLATE,
                                e.getKey().getId(),
                                e.getValue().getMessage()));
                LOG.info("dispatcher cleared for paths " + ArrayUtils.toString(flushPaths.toArray()));
            } catch (LoginException | ReplicationException e) {
                LOG.error("Error clearing mapped dispatcher paths", e);
            } finally {
                if (resolver != null && resolver.isLive()) {
                    resolver.close();
                }
            }
            LOG.info("dispatcher clear finished");
        }
    }

    private void bindInvalidatorService(final Invalidator invalidator) {
        invalidatorServiceMap.putIfAbsent(invalidator.getInvalidatorServiceId(), invalidator);
    }

    public void unbindInvalidatorService(Invalidator i) {
        if (invalidatorServiceMap.containsKey(i.getInvalidatorServiceId())) {
            invalidatorServiceMap.remove(i.getInvalidatorServiceId());
        }
    }

    public Invalidator getInvalidatorServiceById(String invalidatorId) {
        if (invalidatorServiceMap.containsKey(invalidatorId)) {
            return invalidatorServiceMap.get(invalidatorId);
        }
        return null;
    }

    @Activate
    @Modified
    protected void active(Map<String, Object> properties) {
        Object invalidatorsProp = properties.get(INVALIDATOR_PROPERTY);
        if(invalidatorsProp instanceof String) {
            invalidators = ((String) invalidatorsProp).split(",");
        } else {
            invalidators = (String[]) invalidatorsProp;
        }
    }
}