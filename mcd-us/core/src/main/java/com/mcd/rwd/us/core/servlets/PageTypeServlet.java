package com.mcd.rwd.us.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Designer;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by rakesh_balaiah on 3/4/2016.
 */
@SlingServlet(resourceTypes = { "mcd-rwd-global/components/page/base-page","mcd-us/components/content/search"},
		methods = "GET", selectors = "pagetype", extensions = "json")
public class PageTypeServlet extends SlingSafeMethodsServlet {

	private static final Logger LOG = LoggerFactory.getLogger(PageTypeServlet.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(ApplicationConstants.CONTENT_TYPE_JSON);

		Resource resource = request.getResource();
		Page page = ResourceUtil.getContainerPage(resource);
		JSONArray jsonArray = new JSONArray();

		if(null != page) {
			LOG.debug("Page Path {}", page.getPath());
			Designer designer = request.getResourceResolver().adaptTo(Designer.class);
			Resource designRes = designer.getDesign(page).getContentResource();

			jsonArray = populateJSON(designRes);
		}

		PrintWriter out = response.getWriter();
		out.print(jsonArray);
	}

	private JSONArray populateJSON(Resource designRes) {
		JSONArray jsonArray = new JSONArray();
		if (null != designRes && designRes.getChild("pagecategory") != null) {
			ValueMap properties = designRes.getChild("pagecategory").getValueMap();
			String[] pageTypes = properties.get("type", new String[] {});

			try {

				for (String type : pageTypes) {
					JSONObject obj = new JSONObject();
					obj.put("text", type);
					obj.put("value", type);
					jsonArray.put(obj);
				}
			} catch (JSONException e) {
				LOG.error("JSONException while creating json.", e);
			}
		}
		return jsonArray;
	}
}
