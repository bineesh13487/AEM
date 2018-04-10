package com.mcd.rwd.us.workflow;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.Replicator;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.mcd.rwd.us.core.utils.McdCommonService;

/**
 * This class is to replicate the only Activated DAM Asset.
 */
@Component
@Service
public class MCDDamCustomReplication implements WorkflowProcess {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	@Reference
	private ResourceResolverFactory resolverFactory;

	@Reference
	private Replicator replicator;

	@Reference
	McdCommonService mcdCommonService;

	private String replicationConstant = "cq:lastReplicationAction";
	private String activateConstant = "Activate";


	@Override
	public void execute(WorkItem item, WorkflowSession wfsession, MetaDataMap args) throws WorkflowException {
		
		String payloadPath = item.getWorkflowData().getPayload().toString();
		if (StringUtils.isNotEmpty(payloadPath)) {
			org.apache.sling.api.resource.ResourceResolver resourceResolver = mcdCommonService.getResourceResolver();
			Session replicatesession = resourceResolver.adaptTo(Session.class);
			Node node = resourceResolver.getResource(payloadPath).adaptTo(Node.class);
			assestNodeActivation(node,replicatesession);
		    resourceResolver.close();
		}
	}

	private void assestNodeActivation(Node node, Session replicatesession) {
		if (node != null) {
			try {
				Node contentNode = node.getNode("jcr:content");
				if (contentNode != null && contentNode.hasProperty(replicationConstant) && contentNode
						.getProperty(replicationConstant).getString().equalsIgnoreCase(activateConstant)) {
					log.error(replicationConstant + "Replication status of DAM Asset" + contentNode.getPath());
					activateNode(node, replicatesession);
				}
			} catch (RepositoryException exp) {
				log.error("Error in replicating from MCDDamCustomReplication ", exp);

			}

		}
	}

	private void activateNode(Node node2, Session replicatesession) {
		try {
			ReplicationOptions options = new ReplicationOptions();
			// Do not create new versions as this adds to overhead
			options.setSuppressVersions(true);
			// Avoid sling job overhead by forcing synchronous. Note this will
			// result in serial activation.
			options.setSynchronous(true);
			// Do NOT suppress status update of resource (set replication
			// properties accordingly)
			options.setSuppressStatusUpdate(false);
			replicator.replicate(replicatesession, ReplicationActionType.ACTIVATE, node2.getPath(), options);
			replicatesession.save();
		} catch (ReplicationException | RepositoryException e) {
			log.error("Error occured at ActivateNode function", e);
		}
	}

}
