package com.mcd.rwd.newsroom.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.widgets.NumberField;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.newsroom.core.constants.NewsRoomConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

@Component(name = "articleListing",value = "Article Listing Component", description = "Article Listing Component to show articles in Grid view",
		disableTargeting = true, group = "Newsroom" , path="/content",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleListingHandler {

	@DialogField(name = "./headingText", fieldLabel = "Heading Text",
	fieldDescription = "Enter the text to be displayed as heading.Default is set to 'All News'.")
	@TextField @Named("headingText")
	@Inject @Default(values = NewsRoomConstants.headingText)
	private String headingtext;

	@DialogField(name = "./loadMoreText", fieldLabel = "Load More",
			fieldDescription = "Enter the text for Load More button.Default is set to 'Load More'.")
	@TextField @Default(values = NewsRoomConstants.loadMoreText)
	@Inject @Named("loadMoreText")
	private String loadmoretext;

	@DialogField(defaultValue = NewsRoomConstants.loadMoreCount, name = "./loadMoreCount", fieldLabel = "Load More Count",
			fieldDescription = "Enter result Load Limit in numbers .Default is set to '5'.")
	@NumberField
	@Inject @Named("loadMoreCount")
	private String loadmorevalue;

	@DialogField(defaultValue = NewsRoomConstants.defaultShowItemCount, name = "./defaultShowItemCount", fieldLabel = "Default Show Item Count",
			fieldDescription = "Enter default show item count. Default is set to '5'.")
	@NumberField
	@Inject @Named("defaultShowItemCount")
	private String defaultshowitems;

	@DialogField(name = "./newsType", fieldLabel = "News Type", fieldDescription = "Select Type of news." , required = true)
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Regional" , value="Regional"),
					@Option(text="Local" , value="Local")
			})
	@Named("newsType") @Default(values = StringUtils.EMPTY)
	@Inject
	private String newstype;

	@DialogField(defaultValue = NewsRoomConstants.cutoffTitleCount, name = "./cutofftitleCount",
			fieldLabel = "Cut off Count for News Title",
			fieldDescription = "\t\n Enter cut off count for News Title character.Default is set to '150'.")
	@NumberField
	@Inject @Named("cutofftitleCount")
	private String cutofftitlecount;

	@DialogField(defaultValue = NewsRoomConstants.cutoffDescriptionCount, name = "./cutoffDescriptionCount",
			fieldLabel = "Cut off Count for News Description  ",
			fieldDescription = "\t\n Enter cut off  count for News Description character.Default is set to '256'.")
	@NumberField
	@Inject @Named("cutoffDescriptionCount")
	private String cutoffdescriptioncount;

	@DialogField(name = "./noMoreNewsText", fieldLabel = "No More News Text",
			fieldDescription = "This text would be displayed if no news found.Default is set to 'No More News'.")
	@TextField @Named("noMoreNewsText")
	@Inject @Default(values = NewsRoomConstants.noMoreNewsText)
	private String nomorenewstext;

	@DialogField(name = "./newsFolderPath", fieldLabel = "News Folder Path", required = true,
			fieldDescription = "\t\n Enter folder path for news.")
	@PathField @Named("newsFolderPath")
	@Inject @Default(values = StringUtils.EMPTY)
	private String folderpathfornews;

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleListingHandler.class);

	private String headingText;
    private String loadMoreText;
    private String loadMoreCount;
    private String defaultShowItemCount;
    private String newsType;
    private String cutoffDescriptionCount;
	private String cutoffTitleCount;
    private String noMoreNewsText;
    private String newsFolderPath;
 
    /**
     * Method to perform Post Initialization Tasks.
     * @throws IOException 
     */
    @PostConstruct
    public final void activate() throws IOException {
        LOGGER.debug("In Article Listing Component Handler");

        headingText = this.headingtext;
        loadMoreText = this.loadmoretext;
        loadMoreCount = this.loadmorevalue;
        defaultShowItemCount = this.defaultshowitems;
        newsType = this.newstype;
        cutoffDescriptionCount = this.cutoffdescriptioncount;
		cutoffTitleCount = this.cutofftitlecount;
		noMoreNewsText = this.nomorenewstext;
        newsFolderPath = this.folderpathfornews;
        
    }


	public String getHeadingText() {
		return headingText;
	}

	public void setHeadingText(String headingText) {
		this.headingText = headingText;
	}

	public String getLoadMoreText() {
		return loadMoreText;
	}

	public void setLoadMoreText(String loadMoreText) {
		this.loadMoreText = loadMoreText;
	}

	public String getLoadMoreCount() {
		return loadMoreCount;
	}

	public void setLoadMoreCount(String loadMoreCount) {
		this.loadMoreCount = loadMoreCount;
	}

	public String getDefaultShowItemCount() {
		return defaultShowItemCount;
	}

	public void setDefaultShowItemCount(String defaultShowItemCount) {
		this.defaultShowItemCount = defaultShowItemCount;
	}

	public String getNewsType() {
		return newsType;
	}

	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}

	public String getCutoffDescriptionCount() {
		return cutoffDescriptionCount;
	}

	public String getCutoffTitleCount() {
		return cutoffTitleCount;
	}

	public void setCutoffDescriptionCount(String cutoffDescription) {
		this.cutoffDescriptionCount = cutoffDescription;
	}

	public String getNoMoreNewsText() {
		return noMoreNewsText;
	}

	public void setNoMoreNewsText(String noMoreNewsText) {
		this.noMoreNewsText = noMoreNewsText;
	}

	public String getNewsFolderPath() {
		return newsFolderPath;
	}

	public void setNewsFolderPath(String newsFolderPath) {
		this.newsFolderPath = newsFolderPath;
	}


}

