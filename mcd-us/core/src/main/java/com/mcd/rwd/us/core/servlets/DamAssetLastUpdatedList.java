package com.mcd.rwd.us.core.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.McdDynamicMediaServiceConfig;
import com.mcd.rwd.us.core.utils.McdCommonService;

@SuppressWarnings("serial")
@SlingServlet(paths = { "/services/mcd/asset/list" }, methods = { "GET",
		"POST" }, selectors = "akamailist", extensions = "json")
@Properties({ @Property(name = "service.pid", value = "com.mcd.rwd.us.core.servlets.DamAssetLastUpdatedList"),
		@Property(name = "service.description", value = "Provides the list of last updated DAM Asset "),
		@Property(name = "service.vendor", value = "HCL Technologies") })
public class DamAssetLastUpdatedList extends SlingAllMethodsServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(DamAssetLastUpdatedList.class);

	@Reference
	private transient QueryBuilder queryBuilder;

	@Reference
	private transient McdCommonService mcdCommonService;

	@Reference
	private transient McdDynamicMediaServiceConfig mcdDynamicMediaServiceConfig;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		ResourceResolver resourceResolver;
		List<String> damAssetList = new ArrayList<String>();
		response.setContentType(ApplicationConstants.CONTENT_TYPE_JSON);
		if (request.getParameter("timeinterval") != null && request.getParameter("damAssetPath") != null
				&& request.getParameter("requireAllRendition") != null && mcdCommonService.isPublishMode()) {
			int timeInterval = Integer.parseInt(request.getParameter("timeinterval"));
			String damAssestPath = request.getParameter("damAssetPath");
			String assetAllUrlRequired = request.getParameter("requireAllRendition");
			final Map<String, String> constarint = new HashMap<String, String>();
			constarint.put("type", ApplicationConstants.DAMASSETLIST_CONSTANT);
			constarint.put("path", damAssestPath);
			constarint.put("relativedaterange.property", ApplicationConstants.JCR_LASTMODIFIED_CONSTANT);
			constarint.put("relativedaterange.lowerBound", "-" + timeInterval + "m");
			resourceResolver = mcdCommonService.getResourceResolver();
			if (resourceResolver != null) {
				try {
					final Query query = queryBuilder.createQuery(PredicateGroup.create(constarint),
							resourceResolver.adaptTo(Session.class));
					final SearchResult result = query.getResult();
					final Iterator<Node> nodeItr = result.getNodes();
					prepareDamAssetList(damAssetList, nodeItr, assetAllUrlRequired);
				} catch (RepositoryException e) {
					LOGGER.error("Error in Fetching the Asset path from DAM", e);
				} finally {
					resourceResolver.close();
					LOGGER.debug("CLOSED Resource Resolver IN EXECUTE CONTENT QUERY.");

				}
			}
			LOGGER.error("Total number of Dam assest updated  ", damAssetList.size());
			Gson gson = new Gson();
			response.getWriter().print(gson.toJson(damAssetList));
		}

	}

	private void prepareDamAssetList(List<String> damAssetList, final Iterator<Node> nodeItr,
			String assetAllUrlRequired) throws RepositoryException {
		List<String> dynamicImageList = new ArrayList<String>();
		while (nodeItr.hasNext()) {
			Node tempNode = nodeItr.next();
			damAssetList.add(tempNode.getPath());
			LOGGER.error("Node added in list", tempNode.getPath());
		}
		LOGGER.error("Total number of Dam assest before allAssetUrl  ", damAssetList.size());
		if (assetAllUrlRequired.equalsIgnoreCase(com.mcd.rwd.global.core.constants.ApplicationConstants.FLAG_TRUE)) {
			String imageServerAppender = mcdDynamicMediaServiceConfig.getDynamicMediaImageServerappender();
			List<String> imageType = new ArrayList<String>();
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetHeroLarge());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetHeroMedium());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetHeroSmall());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetHeroXSmall());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetIconicThumbnailLarge());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetIconicThumbnailMedium());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetIconicThumbnailSmall());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetIconicThumbnailXSmall());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetThumbnailLarge());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetThumbnailMedium());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetThumbnailSmall());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetThumbnailXSmall());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetLocalOptionLarge());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetLocalOptionMedium());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetLocalOptionSmall());
			imageType.add(mcdDynamicMediaServiceConfig.getImagePresetLocalOptionXSmall());
			for (String asset : damAssetList) {
				for (String type : imageType) {
					dynamicImageList.add(imageServerAppender + asset + type);
				}
			}
		}
		damAssetList.addAll(dynamicImageList);
	}

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		LOGGER.error("Testing");

	}
}
