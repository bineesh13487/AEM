package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.*;
import com.day.cq.wcm.api.Page;
import com.icfolson.aem.multicompositeaddon.widget.MultiCompositeField;
import com.mcd.rwd.global.core.components.common.Image;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.search.Hit;
import com.mcd.rwd.us.core.bean.search.SearchFilter;
import com.mcd.rwd.us.core.constants.SearchConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.xss.XSSAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Seema Pandey on 10-05-2016.
 */

@Component(
		name = "search",
		value = "Search",
		path = "content",
		description = "Enables the ability for the end user to search for any text in the website.",
		allowedParents = {"*/parsys"},
		resourceSuperType = "foundation/components/parbase",
		group = " GWS-Global",
		tabs = {
				@Tab(title = "Desktop Hero Image"),
				@Tab(title = "Mobile Hero Image"),
				@Tab(title = "Default Image"),
				@Tab(title = "General Settings"),
				@Tab(title = "Stop Words")
		}
)
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public final class SearchHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchHandler.class);

	private static final String HERO_KEY = "hero";

	private static final String MOBILE_HERO_KEY = "mobile";

	private static final String DEFAULT_KEY = "default";

	@DialogFieldSet(title = "Desktop Hero Image", namePrefix = HERO_KEY + "/")
	@DialogField(tab = 1)
	@ChildResource(name = HERO_KEY)
	@Inject
	private Image desktopImage;

	@DialogFieldSet(title = "Mobile Hero Image", namePrefix = MOBILE_HERO_KEY + "/")
	@DialogField(tab = 2)
	@ChildResource(name = MOBILE_HERO_KEY)
	@Inject
	private Image mobileImage;

	@DialogFieldSet(title = "Default Image", namePrefix = DEFAULT_KEY + "/")
	@DialogField(tab = 3)
	@ChildResource(name = DEFAULT_KEY)
	@Inject
	private Image defaultImage;

	@DialogField(tab = 4, fieldLabel = "Search Page Title", fieldDescription = "Please provide title for search page.")
	@TextField
	@Inject @Default(values = "Search")
	private String title;

	@DialogField(tab = 4, fieldLabel = "Default Text to show in search box",
			fieldDescription = "Please provide the placeholder text. Defaults to 'Search'", name = "./placeholder")
	@TextField
	@Inject @Named("placeholder") @Default(values = "Search mcdonalds.com")
	private String placeHolder;

	@DialogField(tab = 4, fieldLabel = "Enter text to display on button to Search",
			fieldDescription = "Enter text to display on button for Search", name = "./searchbuttontext")
	@TextField
	@Inject @Named("searchbuttontext") @Default(values = "Submit")
	private String searchButtonText;

	@DialogField(tab = 4, fieldLabel = "Search Result Text", fieldDescription = "Enter search result text. " +
			"Use annotations for the placeholder of searched text as:&#xa;{0}/{1}. Defaults to '{0} result(s) for {1}'")
	@TextField
	@Inject @Default(values = "{0} result(s) for {1}")
	private String resultText;

	@DialogField(tab = 4, fieldLabel = "No Search Query Text", fieldDescription = "Error message to be displayed if " +
			"no search text is entered.", name = "./nosearchquerytext", required = true)
	@TextField
	@Inject @Named("nosearchquerytext") @Default(values = "Please Enter Search Text")
	private String noSearchQueryText;

	@DialogField(tab = 4, fieldLabel = "Limit", fieldDescription = "Enter number of search results to be displayed " +
			"on search page. Minimum is 5, Maximum is 40.", required = true)
	@NumberField(min = "5", max = "40")
	@Inject @Default(intValues = 5)
	private int limit;

	@DialogField(tab = 4, fieldLabel = "Description cut off length", fieldDescription = "Please select cut off " +
			"length after which the search description should be collapsed. Minimum is 50, Maximum is 256.",
			name = "./description_cutoff")
	@NumberField(min = "50", max = "256")
	@Inject @Named("description_cutoff") @Default(intValues = 256)
	private int descriptionCutOff;

	@DialogField(tab = 4, fieldLabel = "Enable search in news section",
			fieldDescription = "Check this to show the news section search in hero area.", name = "./enablenewssearch",
			additionalProperties = {
				@Property(name = "itemId", value = "enablenewssearch"), @Property(name = "value", value = "true")},
			listeners = {
				@Listener(name = "loadcontent", value = "function(tab,value,isChecked){ " +
						"if (tab.findParentByType('panel').getComponent('enablenewssearch').value==true) {\n" +
						"        tab.findParentByType('panel').getComponent('newssearchtext').show();\n" +
						"        tab.findParentByType('panel').getComponent('newsurl').show();\n" +
						"    }\n" +
						"    else {\n" +
						"        tab.findParentByType('panel').getComponent('newssearchtext').hide();\n" +
						"        tab.findParentByType('panel').getComponent('newsurl').hide();\n" +
						"    } }"),
				@Listener(name = "check", value = "function(tab,isChecked){ if (isChecked==true) {\n" +
						"        tab.findParentByType('panel').getComponent('newssearchtext').show();\n" +
						"        tab.findParentByType('panel').getComponent('newsurl').show();\n" +
						"    }\n" +
						"    else {\n" +
						"        tab.findParentByType('panel').getComponent('newssearchtext').hide();\n" +
						"        tab.findParentByType('panel').getComponent('newsurl').hide();\n" +
						"    } }")
	})
	@CheckBox(text = "Enable search in news section")
	@Inject @Named("enablenewssearch") @Default(booleanValues = false)
	private Boolean enableNewsSearch;


	@DialogField(tab = 4, fieldLabel = "News Search Text", fieldDescription = "Please provide the text for searching " +
			"keyword in news section.Use annotations for the placeholder of searched text as:{0}",
			name = "./newssearchtext", additionalProperties = {@Property(name = "itemId", value = "newssearchtext")})
	@TextField
	@Inject @Named("newssearchtext") @Default(values = "Do you want to search {0} in news section?")
	private String newsSearchText;

	@DialogField(tab = 4, fieldLabel = "News Page URL", fieldDescription = "Please provide URL to link with news " +
			"search. URL should be in www.example.xxx/ www.example.xx.yy", name = "./newsurl",
			additionalProperties = @Property(name = "itemId", value = "newsurl"))
	@PathField
	@Inject @Named("newsurl") @Default(values = "news.mcdonalds.com")
	private String newsUrl;

	@DialogField(tab = 4, fieldLabel = "Button text for Load More results", fieldDescription = "Please provide text " +
			"for the 'Load More Results' button.", name = "./loadmore")
	@TextField
	@Inject @Named("loadmore") @Default(values = "Load More")
	private String loadMoreText;

	@DialogField(tab = 4, fieldLabel = "No More Results Text", fieldDescription = "Error message to be displayed " +
			"if no more search results available.", name = "./nomoreresults", required = true)
	@TextField
	@Inject @Named("nomoreresults") @Default(values = "No More Result Found")
	private String noMoreResultsText;

	@DialogField(tab = 4, fieldLabel = "Check to show filter section", fieldDescription = "Check this to " +
			"show filter section", additionalProperties = {
			@Property(name = "itemId", value = "showFilters"), @Property(name = "value", value = "true")
	})
	@CheckBox(text = "Check to show filter section")
	@Inject @Default(booleanValues = false)
	private Boolean showFilters;

	@DialogField(tab = 4, fieldLabel = "Filter Label", fieldDescription = "Please provide text for Filter Label.",
			name = "./filterlabel")
	@TextField
	@Inject @Named("filterlabel") @Default(values = "Filter By:")
	private String filterLabel;

	@DialogField(tab = 4, fieldLabel = "Filter Button Text", fieldDescription = "Please provide text for " +
			"Filter Button Text.", name = "./filterbuttontext")
	@TextField
	@Inject @Named("filterbuttontext") @Default(values = "Go")
	private String filterButtonText;

	@DialogField(tab = 4, fieldLabel = "Filter Dropdown Default Text", fieldDescription = "Default text to be " +
			"displayed in filter dropdown", name = "./filterdefaulttext", required = true)
	@TextField
	@Inject @Named("filterdefaulttext") @Default(values = "All")
	private String filterDefaultText;

	@DialogField(tab = 4, fieldLabel = "Search Filters", fieldDescription = "To add items in filter for search",
			name = "./filters")
	@MultiCompositeField
	@ChildResource(name = "filters")
	@Inject
	private List<SearchFilter> filterList;

	@DialogField(tab = 5, fieldLabel = "Stop Words", fieldDescription = "Please provide the keywords " +
			"which are not allowed in Seach Query. These literals will be replaced by blank string.")
	@MultiField
	@TextField
	@Inject
	private String[] stopWords;


	@DialogField(tab = 5, fieldLabel = "Hidden Page", fieldDescription = "Please provide the path of Page that must " +
			"not be shown in search results.")
	@PathField
	@Inject
	private String hiddenPage;

	@Inject
	private SlingHttpServletRequest request;

	@Inject
	@Via("request")
	private Resource resource;

	@Inject
	private Page currentPage;

	@OSGiService
	XSSAPI xssapi;

	private String desktopHeroImagePath;

	private String mobileHeroImagePath;

	private String defaultImagePath;

	private String searchBoxData;

	private List<Hit> hits = new ArrayList<Hit>();

	private String pageName;

    /**
     * Method to perform Post Initialization Tasks.
     */
	@PostConstruct
	public void activate() {

		LOGGER.debug("SearchHandler Activate Method Called..");

		this.desktopHeroImagePath = null != desktopImage ? desktopImage.getImagePath() : null;
		this.mobileHeroImagePath = null != mobileImage ? mobileImage.getImagePath() : null;
		this.defaultImagePath = null != defaultImage ? defaultImage.getImagePath() : null;
		this.resultText = this.resultText == null ? resultText : resultText.trim();
		this.pageName = PageUtil.getPageNameForAnalytics(currentPage, request);

		if(StringUtils.isNotBlank(hiddenPage)){
			hiddenPage = (hiddenPage.substring(hiddenPage.lastIndexOf('/') + 1)) + ".html";
		}
	}

	/**
	 * Populates the SearchFilters with the values authored.
	 *
	 * @param filters The list of channels.
	 */
	private void populateSearchFilter(final List<Map<String, String>> filters) {
		LOGGER.debug("Inside populateSearchFilter to populate search filters..");
		if (filters != null && !filters.isEmpty()) {
			LOGGER.debug("Number of search filters configured {}", filters.size());
			Iterator<Map<String, String>> itr = filters.iterator();
			String target;
			while (itr.hasNext()) {
				Map<String, String> item = itr.next();
				SearchFilter searchFilter = new SearchFilter();
				searchFilter.setType(item.get("type"));
				searchFilter.setPath(
						(item.get("path") != null && !(StringUtils.EMPTY.equals(item.get("path")))) ?
								LinkUtil.getHref(item.get("path"), String.valueOf('#')) :
								"#");

				target = Boolean.valueOf(item.get("target")) ? "_blank" : "_self";
				searchFilter.setTarget(target);
				this.filterList.add(searchFilter);
			}
		} else {
			LOGGER.debug("No filters configured for search. filterList is empty..");
		}
	}
	
	/**
	 * Method used to format and filter query text entered by user for XSS vulnerability.
	 *
	 * @return Encoded query string
	 */
	@SuppressWarnings("static-access")
	public String getSearchBoxData() {
		try {
			LOGGER.debug("Inside getSearchBoxData to return searched text after filter XSS..");
			RequestParameter reqParam = request.getRequestParameter("q");
			if (reqParam != null) {
				if(request.getRequestURL().toString().contains("/content/")){
					searchBoxData = xssapi.filterHTML(reqParam.getString("UTF-8"));
				}
				else {
					searchBoxData = new java.net.URLDecoder().decode(request.getParameter("q"), "UTF-8");
				}
				LOGGER.info("@@@@@@@@ searchBoxData1 @@@@@@@ "+searchBoxData);
				searchBoxData = checkStopWords(searchBoxData);
				LOGGER.info("@@@@@@@@ searchBoxData2 @@@@@@@ "+searchBoxData);
			}
		} catch (UnsupportedEncodingException ex) {
			LOGGER.error(
					"Unsupported Encoding Exception occurred while getting query parameter in getSearchBoxData(): ",
					ex);
		}
		return searchBoxData;
	}

	/**
	 * Method used to strip stop words from query text.
	 *
	 * @param queryText Text entered by the end user
	 * @return String without stop words
	 */
	private String checkStopWords(String queryText) {
		LOGGER.debug("Inside checkStopWords to return string without stop words..");
		if(null!= stopWords && stopWords.length>0) {
			for(int i=0; i<stopWords.length; i++) {
				if(queryText.indexOf(Pattern.quote(stopWords[i])) > -1) {
					queryText = queryText.replaceAll(Pattern.quote(stopWords[i]), SearchConstants.BLANK).trim();
				}
			}
		}
		return queryText;
	}

	public static void main(String[] args) {

	}

	public List<SearchFilter> getFilterList() {
		return filterList;
	}

	public String getDesktopHeroImagePath() {
		return desktopHeroImagePath;
	}

	public String getMobileHeroImagePath() {
		return mobileHeroImagePath;
	}

	public String getDefaultImagePath() {
		return defaultImagePath;
	}

	public String getTitle() {
		return title;
	}

	public String getPlaceHolder() {
		return placeHolder;
	}

	public String getSearchButtonText() {
		return searchButtonText;
	}

	public String getResultText() {
		return resultText;
	}

	public String getNoSearchQueryText() {
		return noSearchQueryText;
	}

	public int getDescriptionCutOff() {
		return descriptionCutOff;
	}
	
	public Boolean getShowFilters() {
		return showFilters;
	}

	public String getFilterLabel() {
		return filterLabel;
	}

	public String getFilterButtonText() {
		return filterButtonText;
	}

	public Boolean getEnableNewsSearch() {
		return enableNewsSearch;
	}

	public String getNewsSearchText() {
		return newsSearchText;
	}

	public String getNewsUrl() {
		return newsUrl;
	}

	public String getLoadMoreText() {
		return loadMoreText;
	}

	public String getNoMoreResultsText() { 
		return noMoreResultsText; 
	}

	public String getFilterDefaultText() {
		return filterDefaultText; 
	}

	public int getLimit() {
		return limit;
	}

	public List<Hit> getHits() {
		return hits;
	}

	public String getPageName() { return pageName; }

	public String getHiddenPage() {
		return hiddenPage;
	}
}
