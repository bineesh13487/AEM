package com.mcd.akamai.core.flush;

import com.day.cq.replication.AgentConfig;
import com.mcd.akamai.core.exception.AkamaiFlushException;

public interface AkamaiFlushService {

    void flush(AkamaiFlushRequest request) throws AkamaiFlushException;

    void test(AkamaiFlushRequest request) throws AkamaiFlushException;

    void populateAgentConfigParameters(final AgentConfig config, final AkamaiFlushRequest request);

    void populateDefaultAgentConfigParameters(final AkamaiFlushRequest request);

}
