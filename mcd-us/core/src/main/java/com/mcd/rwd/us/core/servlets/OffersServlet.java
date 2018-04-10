package com.mcd.rwd.us.core.servlets;

import com.day.cq.commons.RangeIterator;
import com.day.cq.tagging.TagManager;
import com.google.gson.Gson;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import com.mcd.rwd.us.core.bean.offers.Offer;
import com.mcd.rwd.us.core.bean.offers.OffersResponse;
import com.mcd.rwd.us.core.utils.OfferUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rakesh_balaiah on 3/4/2016.
 */
@SlingServlet(resourceTypes = {"mcd-us/components/content/offers"},
        methods = "GET", selectors = "personalize", extensions = "json")
public class OffersServlet extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(OffersServlet.class);

    private transient List<Offer> localOffers;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(ApplicationConstants.CONTENT_TYPE_JSON);
        int pos = 0;
        String tagId = request.getParameter("location");

        if (StringUtils.isNotBlank(tagId)) {

            if (StringUtils.isNotBlank(request.getParameter("pos"))) {
                pos = Integer.parseInt(request.getParameter("pos"));
            }

            if (pos < 1) {
                pos = 0;
            }

            Resource resource = request.getResource();
            findOffers(resource, tagId, ++pos, request);
        }

        Gson gson = new Gson();
        OffersResponse offersResponse = new OffersResponse();
        offersResponse.setOffers(localOffers);
        response.getWriter().print(gson.toJson(offersResponse));
    }

    private void findOffers(Resource resource, String tagId, int pos, SlingHttpServletRequest request) {
        TagManager manager = resource.getResourceResolver().adaptTo(TagManager.class);
        ValueMap properties = resource.getValueMap();
        String rootPath = properties.get(ApplicationConstants.PN_PATH, String.class);
        String validityText = properties.get("validityText", "Offer Valid: {0} to {1}");
        RangeIterator<Resource> resources = manager.find(rootPath, new String[]{tagId});
        int position = pos;

        if (resources != null) {
            localOffers = new ArrayList<Offer>();
            String pageTitle = PageUtil.getPageNameForAnalytics(ResourceUtil.getContainerPage(resource), request);
            while (resources.hasNext()) {
                Resource res = resources.next();
                LOG.debug("Found local offer {}", res.getPath());
                Offer offer = OfferUtil.getOffer(res.getChild("promo"), validityText);

                if (offer != null) {
                    offer.setAnalytics("OfferBlock:" + pageTitle + ":" + offer.getLink().getText() + ":" + position++);
                    this.localOffers.add(offer);
                }
            }
        }
    }
}
