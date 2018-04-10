package com.mcd.rwd.us.core.datasource;

import com.day.cq.commons.PathInfo;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Designer;
import com.icfolson.aem.library.api.request.ComponentServletRequest;
import com.icfolson.aem.library.core.servlets.datasource.AbstractOptionsDataSourceServlet;
import com.icfolson.aem.library.core.servlets.optionsprovider.Option;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeepc on 03/07/17.
 */
@SlingServlet(resourceTypes = {
        "mcd-rwd-global/datasource/base-page",
        "mcd-us/datasource/search"
    }, methods = "GET"
)
public class FilterPageTypeDataSource extends AbstractOptionsDataSourceServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterPageTypeDataSource.class);

    @Override
    protected List<Option> getOptions(ComponentServletRequest componentServletRequest) {
        List<Option> options = null;
        SlingHttpServletRequest request = componentServletRequest.getSlingRequest();
        String resourcePath = null;
        if (null != request.getParameter("item")) {
            resourcePath = request.getParameter("item");
        }
        else {
            resourcePath = new PathInfo((String)
                    request.getAttribute("javax.servlet.include.path_info")).getSuffix();
        }
        if (StringUtils.isNotEmpty(resourcePath)) {
            Resource resource = componentServletRequest.getResourceResolver().getResource(resourcePath);
            Page page = ResourceUtil.getContainerPage(resource);

            if(null != page) {
                LOGGER.debug("Page Path {}", page.getPath());
                Designer designer = componentServletRequest.getResourceResolver().adaptTo(Designer.class);
                Resource designRes = designer.getDesign(page).getContentResource();
                options = populateOptions(designRes);
            }
        }

        return options;
    }

    private List<Option> populateOptions(Resource designRes) {
        List<Option> optionsList = new ArrayList<>();
        optionsList.add(new Option("", "--- Select ---"));
        if (null != designRes && designRes.getChild("pagecategory") != null) {
            ValueMap properties = designRes.getChild("pagecategory").getValueMap();
            String[] pageTypes = properties.get("type", new String[] {});
            for (String type : pageTypes) {
                Option option = new Option(type, type);
                optionsList.add(option);
            }
        }
        return optionsList;
    }
}
