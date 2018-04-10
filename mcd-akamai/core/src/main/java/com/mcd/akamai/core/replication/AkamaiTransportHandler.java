package com.mcd.akamai.core.replication;

import com.day.cq.replication.*;
import com.mcd.akamai.core.exception.AkamaiFlushException;
import com.mcd.akamai.core.flush.AkamaiFlushRequest;
import com.mcd.akamai.core.flush.AkamaiFlushService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;

import java.io.IOException;

@Service(TransportHandler.class)
@Component(label = "Akamai Purge Agent", immediate = true)
public class AkamaiTransportHandler implements TransportHandler {

    /** Protocol for replication agent transport URI that triggers this transport handler. */
    private final static String AKAMAI_PROTOCOL = "akamai://";

    @Reference
    private AkamaiFlushService flushService;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(AgentConfig config) {
        final String transportURI = config.getTransportURI();

        return (transportURI != null) ? transportURI.toLowerCase().startsWith(AKAMAI_PROTOCOL) : false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReplicationResult deliver(TransportContext ctx, ReplicationTransaction tx)
            throws ReplicationException {

        final AkamaiFlushRequest request = new AkamaiFlushRequest();
        flushService.populateAgentConfigParameters(ctx.getConfig(), request);

        try {
            final String content = IOUtils.toString(tx.getContent().getInputStream());

            if (StringUtils.isNotBlank(content)) {
                final JSONArray purgeObjects = new JSONArray(content);
                for (int i = 0; i < purgeObjects.length(); i++) {
                    request.getArls().add(purgeObjects.get(i).toString());
                }
            }
        } catch (IOException | JSONException e) {
            throw new ReplicationException("Error building Akamai flush request", e);
        }

        final ReplicationActionType replicationType = tx.getAction().getType();

        try {
            switch (replicationType) {
                case ACTIVATE:
                case DEACTIVATE:
                case DELETE:
                    flushService.flush(request);
                    return ReplicationResult.OK;
                case TEST:
                    flushService.test(request);
                    return ReplicationResult.OK;
                default:
                    throw new ReplicationException("Replication action type " + replicationType + " not supported.");
            }
        } catch (AkamaiFlushException e) {
            throw new ReplicationException("Error communicating with Akamai: " + e.getMessage(), e);
        }
    }
}
