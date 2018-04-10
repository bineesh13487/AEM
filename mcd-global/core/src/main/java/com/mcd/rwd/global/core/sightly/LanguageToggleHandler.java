package com.mcd.rwd.global.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Style;
import com.day.text.Text;
import com.mcd.rwd.global.core.utils.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prahlad.d on 3/10/2016.
 */

@Component(
		name = "languagetoggle",
		value = "Language Toggle",
		path = "content",
		description = "Displays the page details in case available in multiple languages.",
		group = ".hidden"
)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LanguageToggleHandler { //extends McDUse {

	private static final Logger LOG = LoggerFactory.getLogger(LanguageToggleHandler.class);

	private Map<String, String> pages = new LinkedHashMap<String, String>();
	
	private String pageNameForAnalytics = StringUtils.EMPTY;
	
	private String pageName = StringUtils.EMPTY;
	
	private List<String> langList = new ArrayList<String>();
	
	private List<Page> LanguageToggleLinksList = new ArrayList<Page>();

	//private List<String> lang = new ArrayList<String>();

	@Inject
	SlingHttpServletRequest request;

	@Inject
	Page currentPage;

	@Inject
	PageManager pageManager;

	@Inject
	Style currentStyle;

	/**
	 * Method to perform Post Initialization Tasks.
	 */
	@PostConstruct
	protected void activate() {

		int depth = 1;
		if(null != currentStyle) {
			Resource designResource = currentStyle.getDefiningResource(StringUtils.EMPTY);
			ValueMap properties = null != designResource ? designResource.getValueMap() : null;
			this.pageNameForAnalytics = PageUtil.getPageNameForAnalytics(currentPage);

			if (StringUtils.equalsIgnoreCase(currentPage.getAbsoluteParent(1).getName(), "prelaunch")) {
				depth++;
			}
			if (null != properties && properties.get("enabled", false) && currentPage.getDepth() > depth) {
				String langSpanish = properties.get("esLang", "");
				String langEnglish = properties.get("enLang", "");
				Iterator<Page> languages = currentPage.getAbsoluteParent(depth)
						.listChildren(new PageFilter(request));
                final ValueMap valueMap = currentStyle.get("languagetoggle", ValueMap.EMPTY);
                String[] LanguageToggleLinks = valueMap.get("languagetogglelinks", String[].class);
                if (properties.get("manual", false)) {
                    if(LanguageToggleLinks != null && LanguageToggleLinks.length > 0) {
                        for(int i = 0; i < LanguageToggleLinks.length; i++) {
                            LanguageToggleLinksList.add(pageManager.getPage(LanguageToggleLinks[i]));
                        }
                        languages = getLanguageToggleLinksList().iterator();
                    }
                }
                else {
                    languages = currentPage.getAbsoluteParent(depth).listChildren(new PageFilter(request));
                }

				String requestURI = request.getRequestURI();
				String vanity = StringUtils.EMPTY;
				Resource res = request.getResourceResolver().getResource(requestURI);
				if ((res != null) && !StringUtils.endsWith(res.getPath() + ".html", requestURI)) {
					LOG.debug("Looks like a product page / contains selectors");
					vanity += findVanityString(res.getPath(), requestURI);
				}

				final PageManager manager = pageManager;
				final String currentLocale = Text.getAbsoluteParent(currentPage.getPath(), ++depth);
				while (languages.hasNext()) {
					retrieveLanguagePage(currentPage, langSpanish, langEnglish, languages, vanity, manager, currentLocale);
				}
			}
        }
	}

	private void retrieveLanguagePage(final Page currentPage, String langSpanish, String langEnglish,
			Iterator<Page> languages, String vanity, final PageManager manager, final String currentLocale) {
		Page page;
		Page localePage;
		page = languages.next();
		if (!isCurrentPage(currentLocale, page)) {
			localePage = manager.getPage(
					StringUtils.replace(currentPage.getPath(), currentLocale, page.getPath()));
			this.pages.put(retrievePagePath(localePage, page, vanity), PageUtil.getNavTitle(page));
			if(!PageUtil.getNavTitle(page).equals("English")){
				this.langList.add(langSpanish);
			}else{
				this.langList.add(langEnglish);
			}

		}
	}

	/**
	 * @param localePage    Page
	 * @param page 			Page
	 * @return boolean
	 */
	private String retrievePagePath(final Page localePage, final Page page, final String vanity) {
		if (null != localePage) {
			return localePage.getPath() + vanity;
		} else {
			return page.getPath();
		}
	}

	/**
	 * @param currentLocale String
	 * @param page          Page
	 * @return boolean
	 */
	private boolean isCurrentPage(final String currentLocale, final Page page) {
		return StringUtils.equals(currentLocale, page.getPath());
	}

	private String findVanityString(final String resourcePath, final String requestURI) {
		int index = resourcePath.indexOf('/');
		String path = resourcePath;
		String uri = requestURI;
		do {
			if (index != -1 && uri.startsWith(path)) {
				uri = uri.replace(path, StringUtils.EMPTY);
				LOG.debug("Vanity String found {}", uri);
				return uri.replace(".html", StringUtils.EMPTY);
			}
			index = path.indexOf('/', index + 1);
			path = path.substring(index, path.length());
		} while (index != -1);
		return StringUtils.EMPTY;
	}

	/**
	 * Getter Method for pages.
	 *
	 * @return The list of pages.
	 */
	public final Map<String, String> getPages() {
		return pages;
	}
	
	public List<String> getLangList() {
		return langList;
		}
	public String getPageName() {
		return pageName;
	}
	
	public List<Page> getLanguageToggleLinksList() {
		return LanguageToggleLinksList;
	}

	public String getPageNameForAnalytics() {
		return pageNameForAnalytics;
	}
}
