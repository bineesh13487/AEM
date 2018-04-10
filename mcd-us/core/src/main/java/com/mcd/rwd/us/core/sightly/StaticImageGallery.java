package com.mcd.rwd.us.core.sightly;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.mcd.rwd.global.core.bean.Link;
import com.mcd.rwd.global.core.bean.html.Image;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.sightly.McDUse;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.ResourceUtil;

/**
 * Created by Mohit Bansal on 06-06-2017.
 */
public class StaticImageGallery extends McDUse {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaticImageGallery.class);

    private List<Image> imageList;

    private boolean enableMultiRow;

    private String Title;

    private String count;
    /**
     * Method to perform Post Initialization Tasks.
     */
    @Override public final void activate() {

        Resource resource = getResource().getChild("customwidget");
        if (resource != null) {
            LOGGER.debug("Static Image Gallery - Custom Widget data exists.");
            ValueMap properties = resource.adaptTo(ValueMap.class);

            String [] titles = properties.get(ApplicationConstants.PN_TITLE, String[].class);
            String [] images = properties.get(ApplicationConstants.RES_IMAGE, String[].class);
            String [] thumbnail = properties.get(ApplicationConstants.RES_THUMBNAIL, String[].class);
            this.enableMultiRow = getProperties().get("enableMultiRow", false);
            this.count = getProperties().get("itemCount", "6");
            this.Title = getProperties().get("componentTitle", "");
            if(titles!=null  && images!=null){
                imageList = new ArrayList<Image>();
                LOGGER.debug("Static Image Gallery = "+titles.length);
                for(int i=0; i <titles.length; i++){
                    Image image = new Image();
                    image.setSrc(images[i]);
                    image.setLink(thumbnail[i]);
                    image.setTitle(titles!=null ? titles[i] : StringUtils.EMPTY);
                    imageList.add(image);
                }
            }

        }
    }

    public boolean isEnableMultiRow() {
        return enableMultiRow;
    }
    public String  getTitle() {
        return Title;
    }
    public String getCount() { return count;   }
    public List<Image> getImageList() {
        return imageList;
    }

}

