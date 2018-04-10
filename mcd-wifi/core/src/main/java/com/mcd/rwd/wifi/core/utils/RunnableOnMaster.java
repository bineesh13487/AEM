package com.mcd.rwd.wifi.core.utils;

import aQute.bnd.annotation.ConsumerType;

import org.apache.sling.discovery.TopologyEvent;
import org.apache.sling.discovery.TopologyEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Vamsi Jetty on 05-05-2016. 
 * Abstract base class for scheduled job to be run only on the Leader.
 */

@ConsumerType
public abstract class RunnableOnMaster implements TopologyEventListener, Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(RunnableOnMaster.class);

	private Boolean isLeaderInstance = Boolean.FALSE;
	
	/**
     * Run the scheduled job.
     */
    protected abstract void runOnMaster(); 
    
    /**
     * Topology Event Handling. This method makes this Listener cluster aware so it only responds to events on the Leader,
     *  Without this every node in the cluster will process the event (generally resulting with duplicate work) 
     */
    @Override
    public void handleTopologyEvent(final TopologyEvent event) {
        if ( event.getType() == TopologyEvent.Type.TOPOLOGY_CHANGED
                || event.getType() == TopologyEvent.Type.TOPOLOGY_INIT) {
            this.isLeaderInstance = event.getNewView().getLocalInstance().isLeader();
            log.info("isLeaderInstance confirmed :::: "+ this.isLeaderInstance);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        if (isLeaderInstance) {
        	runOnMaster();
        }
    }

}
