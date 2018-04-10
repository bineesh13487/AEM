package com.mcd.rwd.global.core.replication;

import com.day.cq.replication.ReplicationAction;
import com.mcd.akamai.core.config.AkamaiDomainConfiguration;
import com.mcd.akamai.core.exception.AkamaiFlushException;
import com.mcd.akamai.core.flush.AkamaiFlushRequest;
import com.mcd.akamai.core.flush.AkamaiFlushService;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
@Component(immediate = true, metatype = true, label = "MCD Dynamic Media Replication Listener")
@Property(name="event.topics",value= ReplicationAction.EVENT_TOPIC)
public class DynamicMediaReplicationListener implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicMediaReplicationListener.class);

    private static final String DEFAULT_PREFIX = "/is/image";

    @Property(label = "Dynamic Media Paths", value = {"/content/dam"})
    private static final String PROP_DYNAMIC_MEDIA_PATHS = "dynamic.media.paths";

    @Property(label = "Dynamic Media Path Prefix", value = DEFAULT_PREFIX)
    private static final String PROP_PATH_PREFIX = "path.prefix";

    @Property(label = "Dynamic Media Variants", value = {"LARGE", "MEDIUM", "SMALL"})
    private static final String PROP_VARIANTS = "variants";

    @Reference
    private SlingSettingsService settingsService;

    @Reference
    private AkamaiFlushService flushService;

    @Reference
    private AkamaiDomainConfiguration domainConfiguration;

    private boolean active;
    private String prefix;
    private List<String> paths = new ArrayList<>();
    private List<String> variants = new ArrayList<>();

    @Override
    public void handleEvent(Event event) {
        if (active) {
            final ReplicationAction replicationAction = ReplicationAction.fromEvent(event);
            final Set<String> replicatedPaths = new HashSet<>();
            if (replicationAction.getPath() != null) {
                replicatedPaths.add(replicationAction.getPath());
            }
            if (replicationAction.getPaths() != null) {
                Collections.addAll(replicatedPaths, replicationAction.getPaths());
            }
            try {
                final AkamaiFlushRequest request = new AkamaiFlushRequest();
                request.getArls().addAll(getAkamaiARLs(replicatedPaths));
                if (!request.getArls().isEmpty()) {
                    flushService.flush(request);
                }
            } catch (AkamaiFlushException e) {
                LOG.error("Error flushing Akamai paths", e);
            }
        }
    }

    @Activate
    @Modified
    protected final void activate(final Map<String, Object> props) {
        active = settingsService.getRunModes().contains("author");
        paths = Arrays.asList(PropertiesUtil.toStringArray(props.get(PROP_DYNAMIC_MEDIA_PATHS), new String[0]));
        prefix = PropertiesUtil.toString(props.get(PROP_PATH_PREFIX), DEFAULT_PREFIX);
        variants = Arrays.asList(PropertiesUtil.toStringArray(props.get(PROP_VARIANTS), new String[0]));
    }

    private Set<String> getAkamaiARLs(final Set<String> replicatedPaths) {

        // filter out non-Dynamic Media paths and add prefix
        final Set<String> paths = new HashSet<>();
        for (String replicatedPath : replicatedPaths) {
            for (String path : this.paths) {
                if (replicatedPath.startsWith(path)) {
                    paths.add(prefix + replicatedPath);
                }
            }
        }

        // Add domains
        final Set<String> urls = new HashSet<>();
        for (String domain : domainConfiguration.getAssetDomains()) {
            for (String path : paths) {
                urls.add(domain + path);
            }
        }

        // Add variants
        final Set<String> urlWithVariant = new HashSet<>();
        for (String url : urls) {
            for (String variant : variants) {
                urlWithVariant.add(url + "?" + variant);
            }
        }

        return urlWithVariant;
    }
}
