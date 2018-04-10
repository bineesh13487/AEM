package com.mcd.rwd.newsroom.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.mcd.rwd.global.core.components.common.Text;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by deepti_b on 09-07-2017.
 */
@Component(name = "articleDescomponent",value = "Article Description Component", description = "Article description Component to display hero image ",
        disableTargeting = true, group = ".hidden" , path="/content",
        listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
                @Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleDescriptionHandler {

    @DialogField(fieldLabel = "Article Description Text", fieldDescription = "Text to be displayed as Article Description.")
    @DialogFieldSet(title = "Article Description Text" , namePrefix = "imagetext/")
    @ChildResource(name = "imagetext")
    @Inject @Named("articleDescription")
    Text imagetext;

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleImageHandler.class);

    private String articleDescription;
    /**
     * Method to perform Post Initialization Tasks.
     */
    @PostConstruct
    public void activate() {
        LOGGER.debug("In Article Description Component Handler");
        if(imagetext!=null)
        articleDescription = imagetext.getText();
    }

    public String getArticleDescription() {
        return articleDescription;
    }

}
