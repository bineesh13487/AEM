package com.mcd.rwd.us.core.servlets;

import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
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
import java.util.*;

/**
 * Created by deepti_b on 16-01-2017.
 */
@SlingServlet(
        paths = {"/services/mcd/exceptionList"},
        methods = {"GET"},extensions = "json"
)
@Properties(value = {
        @Property(name = "service.pid", value = "com.mcd.rwd.us.core.servlets.ExceptionListOffer",
                propertyPrivate = false),
        @Property(name = "service.description", value = "Provides offers info in exception List in json format",
                propertyPrivate = false),
        @Property(name = "service.vendor", value = "HCL Technologies", propertyPrivate = false)})
public class ExceptionListOffer extends SlingSafeMethodsServlet {


    /**
     * default logger
     */
    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String offerPagePath = request.getParameter("pagepath");
       // Resource resource = request.getResource().getResourceResolver().getResource("/content/us/en-us/config/test_offers");
        Resource resource = request.getResource().getResourceResolver().getResource(offerPagePath);
        Page page = ResourceUtil.getContainerPage(resource);
        List offersList=new ArrayList<HashMap<String,String>>();
        Map offersMap=new HashMap<String,List<HashMap>>();
        if (null != page && null != page.getContentResource("offerslist")) {
            Iterator<Resource> childResource = page.getContentResource("offerslist").listChildren();
            while (childResource.hasNext()) {
                    Resource offerResource = childResource.next();
                    if (offerResource.isResourceType("mcd-us/components/content/offerexceptionlist")) {
                        ValueMap offerProperties = offerResource.getValueMap();
                        HashMap offerMap=new HashMap<String,String>();
                        offerMap.put("OfferName", offerProperties.get("offerName", String.class));
                        offerMap.put("OfferCategory",offerProperties.get("offerCategory", String.class));
                        offerMap.put("OfferExpiryDate",offerProperties.get("offerExpiryDate", String.class));
                        offersList.add(offerMap);
                    }
            }
        }
        try {
            offersMap.put("OffersList",offersList);
            String offerJsonString = new Gson().toJson(offersMap);
            response.getWriter().print(offerJsonString);
        } catch (IOException ioe) {
            logger.error("Exception in ExceptionListOffer while writing response", ioe);
        }


    }
}






