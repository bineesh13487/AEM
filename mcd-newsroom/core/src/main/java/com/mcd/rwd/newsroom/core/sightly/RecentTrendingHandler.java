package com.mcd.rwd.newsroom.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.widgets.NumberField;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.day.cq.wcm.api.Page;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.newsroom.core.constants.NewsRoomConstants;
import com.mcd.rwd.newsroom.core.models.ArticleDetailBean;
import com.mcd.rwd.newsroom.core.service.ArticleSearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolverFactory;
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

@Component(name = "recentTrendingComponent",value = "Recent Trending News Component", description = "News Component to show trending or recent mew " +
		"in Slide/List view as per the setup",
		disableTargeting = true, group = "Newsroom" , path="/content",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RecentTrendingHandler {

	@DialogField(name = "./newsTitle", fieldLabel = "News Title Text",
			fieldDescription = "Enter the title for News. Default is set to 'News'.")
	@TextField
	@Named("newsTitle")
	@Inject
	@Default(values = NewsRoomConstants.NewsTitle)
	private String title;

	@DialogField(name = "./readMoreText", fieldLabel = "Read More Text",
			fieldDescription = "Enter Text for  Read More button.Default is set to 'Read More'. ")
	@TextField @Named("readMoreText")
	@Inject @Default(values = NewsRoomConstants.ReadMore)
	private String readmoretext;

	@DialogField(defaultValue = NewsRoomConstants.cutoffTitleCount, name = "./cutofftitleCountrecent",
			fieldLabel = "Cut off Count for News Title ",
			fieldDescription = "\t\n Enter cut off count for News Title character.Default is set to '150'.")
	@NumberField
	@Inject @Named("cutofftitleCountrecent")
	private String cutofftitlecount;

	@DialogField(fieldLabel = "Section", fieldDescription = "Select news section. " , required = true)
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Trending" , value="Trending"),
					@Option(text="Recent" , value="Recent")
			})
	@Default(values = StringUtils.EMPTY)
	@Inject
	private String section;

	@DialogField(name = "./newsFolderPath", fieldLabel = "News Folder Path", required = true,
			fieldDescription = "Enter folder path for news.")
	@PathField
	@Named("newsFolderPath")
	@Inject @Default(values = StringUtils.EMPTY)
	private String folderpathfornews;

	@DialogField(name = "./noOfItemsToShow", fieldLabel = "Number of Item to show", fieldDescription = "Enter Item count" +
			" to show in page. Default is set to '5'.")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="3" , value="3"),
					@Option(text="4" , value="4"),
					@Option(text="5" , value="5"),
					@Option(text="6" , value="6"),
					@Option(text="7" , value="7"),
					@Option(text="8" , value="8"),
					@Option(text="8" , value="8")
			})
	@Named("noOfItemsToShow")
	@Inject @Default(values = NewsRoomConstants.noOfItemsToShow)
	private String noofitemstoshow;

	@DialogField(name = "./noNewsText", fieldLabel = "No More News Text",
			fieldDescription = "This text would be displayed if no news found.")
	@TextField @Named("noNewsText")
	@Inject @Default(values = StringUtils.EMPTY)
	private String nonewstext;

	@DialogField(name = "./viewType", fieldLabel = "View Type", fieldDescription = "\t\n Select view type." , required = true)
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Slider" , value="Slider"),
					@Option(text="List" , value="List")
			})
	@Named("viewType") @Default(values = StringUtils.EMPTY)
	@Inject
	private String view;

	@Inject
	Page currentPage;

	@Inject
	SlingScriptHelper slingScriptHelper;

    private static final Logger LOGGER = LoggerFactory.getLogger(RecentTrendingHandler.class);
   
    @Reference
    private ResourceResolverFactory resolverFactory;
   
    private String newsTitle;
    private String readMoreText;
    //private String section;
    private String noOfItemsToShow;
    private String viewType;
    private String newsFolderPath;
    private String noNewsText;
	private String country;
	private String language;
	private ArticleSearchService articleSearchService;
	private String cutoffTitleCountRecent;


	private List<ArticleDetailBean>  articleDetailList =new ArrayList<ArticleDetailBean>();
    /**
     * Method to perform Post Initialization Tasks.
     * @throws IOException 
     */
    @PostConstruct
    public final void activate() throws IOException {
        LOGGER.debug("In News Component Handler");
		this.country = PageUtil.getCountry(currentPage);
		this.language = PageUtil.getLanguage(currentPage);
        newsTitle = this.title;
		cutoffTitleCountRecent=this.cutofftitlecount;
        readMoreText = this.readmoretext;
        noOfItemsToShow = this.noofitemstoshow;
        viewType = this.view;
        noNewsText = this.nonewstext;
        newsFolderPath = this.folderpathfornews;
		articleSearchService = slingScriptHelper.getService(ArticleSearchService.class);
		if(StringUtils.isNotEmpty(section)){
			articleDetailList=articleSearchService.getArticleResults(section,newsFolderPath,country,language,noOfItemsToShow);

			  for(ArticleDetailBean articleDetail:articleDetailList){
				  if(articleDetail.getArticleTitle().length()>15)
				  articleDetail.setArticleMinTitle(articleDetail.getArticleTitle().substring(0,30)+"...");
				  else
				  articleDetail.setArticleMinTitle(articleDetail.getArticleTitle());

			  }

		}

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

	public String getCutoffTitleCountRecent() {
		return cutoffTitleCountRecent;
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

	public String getNoNewsText() {
		return noNewsText;
	}

	public List<ArticleDetailBean> getArticleDetailList() {
		return articleDetailList;
	}
}