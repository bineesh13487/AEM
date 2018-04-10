package com.mcd.yrtk.servlet;

import com.google.common.net.MediaType;
import com.mcd.yrtk.service.McdHttpConnectionMgrService;
import com.mcd.yrtk.service.YRTKWebServicesConfig;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;

@SlingServlet(paths = "/services/mcd/yrtk-sitemap", methods = "GET", extensions = "xml")
@Properties({
        @Property(name = "service.pid", value = "com.mcd.yrtk.servlet.YRTKSitemap", propertyPrivate = false),
        @Property(name = "service.description",
            value = "Returns the dynamically generated XML sitemap with individual quesion URLs for Arabia websites.",
            propertyPrivate = false)
    })
public final class YRTKSitemap extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(YRTKSitemap.class);

    @Reference
    private YRTKWebServicesConfig yrtkWebServicesConfig;
    @Reference
    private McdHttpConnectionMgrService mcdHttpConnectionMgrService;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(MediaType.APPLICATION_XML_UTF_8.toString());
        try {

            final HttpClient httpClient = new HttpClient(mcdHttpConnectionMgrService.getMultiThreadedConf());

            final String yrtkServiceURL =
                    yrtkWebServicesConfig.getYrtkServicesBaseURL() + "/ofyq/seo/generateSiteMap/ar";

            final GetMethod method = new GetMethod(yrtkServiceURL);

            final int statusCode = httpClient.executeMethod(method);
            final String responseBody = method.getResponseBodyAsString();

            if (statusCode != HttpStatus.SC_OK) {
                LOGGER.warn("call to get XML sitemap was not successful. Response status code: {}\n respone body:\n{}",
                        statusCode, responseBody);
            }

            response.getWriter().print(responseBody);
        } catch (IOException ioe) {
            LOGGER.error(ioe.getMessage(), ioe);
        }
    }
}
