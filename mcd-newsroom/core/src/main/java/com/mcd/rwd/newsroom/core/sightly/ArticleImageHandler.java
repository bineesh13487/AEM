package com.mcd.rwd.newsroom.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.mcd.rwd.global.core.components.common.Image;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by deepti_b on 02-07-2017.
 */
@Component(name = "articleImgcomponent",value = "Article Image Component", description = "Article Image Component to display hero image ",
        disableTargeting = true, group = ".hidden" , path="/content",
        listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
                @Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleImageHandler {

    @DialogField(fieldLabel = "Desktop Image")
    @DialogFieldSet(title = "Desktop Image" , namePrefix = "desktop/")
    @ChildResource(name = "desktop")
    @Inject
    Image desktop;

    public Image getMobile() {
        return mobile;
    }

    public Image getDesktop() {
        return desktop;
    }

    @DialogField(fieldLabel = "Mobile Image")
    @DialogFieldSet(title = "Mobile Image" , namePrefix = "mobile/")
    @ChildResource(name = "mobile")
    @Inject
    Image mobile;

    @Inject
    SlingHttpServletRequest request;

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleImageHandler.class);

    private String imageHref;

    private String mobileImageHref;

    private static final String DESKTOP_IMG = "desktop";

    private static final String MOBILE_IMG = "mobile";

    /**
     * Method to perform Post Initialization Tasks.
     */
    @PostConstruct
    public void activate() {
        LOGGER.debug("In Article Image Component Handler");
        if(desktop!=null){
            imageHref = desktop.getImagePath();
        }
        if(mobile!=null){
            mobileImageHref = mobile.getImagePath();
        }
    }


    public String getImageHref() {
        return imageHref;
    }

    public String getMobileImageHref() {
        return mobileImageHref;
    }

}

