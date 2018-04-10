package com.mcd.rwd.us.core.datasource;

import com.icfolson.aem.library.api.request.ComponentServletRequest;
import com.icfolson.aem.library.core.servlets.datasource.AbstractOptionsDataSourceServlet;
import com.icfolson.aem.library.core.servlets.optionsprovider.Option;
import com.mcd.rwd.us.core.service.NutrientProvider;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

import java.util.List;

/**
 * Created by sandeepc on 29/06/17.
 */

@SlingServlet(resourceTypes = "mcd-us/dataSource/nutrients")
public class NutrientsListDataSource extends AbstractOptionsDataSourceServlet {

    @Reference
    private NutrientProvider nutrientProvider;

    @Override
    protected List<Option> getOptions(ComponentServletRequest componentRequest) {
        final SlingHttpServletRequest request = componentRequest.getSlingRequest();
        final Resource resource = request.getRequestPathInfo().getSuffixResource();
        final String component = request.getParameter("comp");
        return nutrientProvider.getNutrientOptions(resource, component);
    }
}


