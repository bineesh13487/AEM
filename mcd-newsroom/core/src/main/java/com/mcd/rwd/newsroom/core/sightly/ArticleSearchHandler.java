package com.mcd.rwd.newsroom.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.day.cq.wcm.api.Page;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.newsroom.core.models.ArticleDetailBean;
import com.mcd.rwd.newsroom.core.service.ArticleSearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by mahendrakumar.y on 01-08-2017.
 */
@Component(name = "articleSearchComponent",value = "Article Search Component", description = "Article Search Component to fetch all articles," +
        " that will help in fetching search results",
        disableTargeting = true, group = "Newsroom" , path="/content",
        listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
                @Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleSearchHandler {

    @DialogField(name = "./newsFolderPath", fieldLabel = "News Folder Path", required = true,
            fieldDescription = "Enter news folder path.")
    @PathField
    @Named("newsFolderPath")
    @Inject
    @Default(values = StringUtils.EMPTY)
    private String folderpathfornews;

    @Inject
    Page currentPage;

    @Inject
    SlingScriptHelper slingScriptHelper;

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleListingHandler.class);


    private String newsFolderPath;
    private List<ArticleDetailBean> articleDetailList =new ArrayList<ArticleDetailBean>();
    private ArticleSearchService articleService;
    private String country;
    private String language;
    /**
     * Method to perform Post Initialization Tasks.
     * @throws IOException
     */
    @PostConstruct
    public final void activate() throws IOException {
        LOGGER.debug("In Article Search Component Handler");
        this.country = PageUtil.getCountry(currentPage);
        this.language = PageUtil.getLanguage(currentPage);
        articleService = slingScriptHelper.getService(ArticleSearchService.class);
        newsFolderPath = this.folderpathfornews;
        articleDetailList = articleService.getAllArticleList(newsFolderPath,country,language);
    }

    public String getNewsFolderPath() {
        return newsFolderPath;
    }

    public void setNewsFolderPath(String newsFolderPath) {
        this.newsFolderPath = newsFolderPath;
    }
    public List<ArticleDetailBean> getArticleDetailList() {
        return articleDetailList;
    }
}
