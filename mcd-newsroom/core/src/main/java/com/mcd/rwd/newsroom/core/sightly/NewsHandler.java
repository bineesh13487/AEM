package com.mcd.rwd.newsroom.core.sightly;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NewsHandler extends WCMUsePojo {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsHandler.class);
   
    @Reference
    private ResourceResolverFactory resolverFactory;
   
    private String newsTitle;
    private String readMoreText;
    private String section;
    private String noOfItemsToShow;
    private String viewType;
    private String newsFolderPath;
 
    /**
     * Method to perform Post Initialization Tasks.
     * @throws IOException 
     */
    @Override
    public final void activate() throws IOException {
        LOGGER.debug("In News Component Handler");
        
        newsTitle = getProperties().get("newsTitle", "News");
        readMoreText = getProperties().get("readMoreText", "Read More");
        section = getProperties().get("section", StringUtils.EMPTY);
        noOfItemsToShow = getProperties().get("noOfItemsToShow", "5");
        viewType = getProperties().get("viewType", StringUtils.EMPTY);
        newsFolderPath = getProperties().get("newsFolderPath", StringUtils.EMPTY);
        
    }

    
	public String getNewsTitle() {
		return newsTitle;
	}

	public String getReadMoreText() {
		return readMoreText;
	}


	public String getSection() {
		return section;
	}


	public String getNoOfItemsToShow() {
		return noOfItemsToShow;
	}

	public String getViewType() {
		return viewType;
	}

	public String getNewsFolderPath() {
		return newsFolderPath;
	}

	public void setNewsFolderPath(String newsFolderPath) {
		this.newsFolderPath = newsFolderPath;
	}


}