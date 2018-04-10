package com.mcd.rwd.us.workflow;

import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.DamConstants;
import com.day.cq.replication.Replicator;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowService;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.day.cq.workflow.model.WorkflowModel;
import com.mcd.rwd.us.core.utils.McdCommonService;

/**
 * This class is to invoke a custom workflow which will be passed from the arguments
 * It will invoke custom workflow for all child DAM asset node.
 */
@Component
@Service
@SuppressWarnings("unchecked")
public class MCDDamUpdateCustomProcess implements WorkflowProcess {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	@Reference
	private ResourceResolverFactory resolverFactory;

	@Reference
	private WorkflowService workflowService;

	@Reference
	private Replicator replicator;

	@Reference
	McdCommonService mcdCommonService;

	private String workflowPath = "/etc/workflow/models/dam/update_asset/jcr:content/model";

	private static final String WORKFLOWARGUMENT = "PROCESS_ARGS";

	private org.apache.sling.api.resource.ResourceResolver resourceResolver;
	private javax.jcr.Session session;

	@Override
	public void execute(WorkItem item, WorkflowSession wfsession, MetaDataMap args) throws WorkflowException {
		try {
			log.debug("Dam Asset Activation Workflow Activated");
			resourceResolver = mcdCommonService.getResourceResolver();
			String payloadPath = item.getWorkflowData().getPayload().toString();
			if (args.containsKey(WORKFLOWARGUMENT)) {
				workflowPath = args.get(WORKFLOWARGUMENT, String.class);
			}
			log.debug("Payload path for workflow is: " + payloadPath);
			Node node = resourceResolver.getResource(payloadPath).adaptTo(Node.class);
			if (node.hasNodes()) {
				log.debug("Child Node exists");
				session = resourceResolver.adaptTo(javax.jcr.Session.class);
				checkNode(node);
			}
		} catch (RepositoryException e) {
			log.error("Error Occured at execute method", e);

		} finally {
			if (resourceResolver != null && resourceResolver.isLive()) {
				try {
					resourceResolver.commit();
					resourceResolver.close();
				} catch (PersistenceException e) {
					log.error("Error Occured at committing the resource resolver", e);
				}

			}

		}

	}

	private void checkNode(Node node) {
		try {

			String primaryType = node.getPrimaryNodeType().toString();
			log.debug("PrimaryType of Node is: " + primaryType);
			if (DamConstants.NT_DAM_ASSET.equalsIgnoreCase(primaryType)) {
				log.error(" Workflow status of DAM Asset" + node.getPath());
				runDamUpdateAssestWorkFlow(node, session);
			} else {
				Iterator<Node> children = node.getNodes();
				while (children.hasNext()) {
					Node node2 = children.next();
					checkNode(node2);
				}
			}
		} catch (RepositoryException e) {
			log.error("Exception Occured at checkNode Function", e);
		}
	}

	private void runDamUpdateAssestWorkFlow(Node node, javax.jcr.Session session) {
		try {

			WorkflowSession wfSession = workflowService.getWorkflowSession(session);
			WorkflowModel wfModel = wfSession.getModel(workflowPath);

			// Get the workflow data
			// The first param in the newWorkflowData method is the payloadType.
			// Just a fancy name to let it know what type of workflow it is
			// working with.
			WorkflowData wfData = wfSession.newWorkflowData("JCR_PATH", node.getPath());

			// Run the Workflow.
			wfSession.startWorkflow(wfModel, wfData);
			session.save();
		} catch (WorkflowException | RepositoryException e) {
			log.error("Error occured at runDamUpdateAssestWorkFlow function", e);
		}
	}

}
