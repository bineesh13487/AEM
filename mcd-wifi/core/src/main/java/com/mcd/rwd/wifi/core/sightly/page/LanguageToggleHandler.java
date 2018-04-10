package com.mcd.rwd.wifi.core.sightly.page;

import com.citytechinc.cq.component.annotations.Component;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Style;
import com.day.text.Text;
import com.mcd.rwd.global.core.utils.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chandan_b
 * Date : 22-08-2016
 */

@Component(
		name = "languagetoggle",
		value = "Language Toggle",
		path = "content",
		description = "Displays the page details in case available in multiple languages.",
		group = ".hidden"
)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LanguageToggleHandler { //extends McDUse{

	private Map<String, String> pages = new LinkedHashMap<String, String>();
	private Map<String, String> isSelected = new LinkedHashMap<String, String>();

	@Inject
	SlingHttpServletRequest request;

	@Inject
	Page currentPage;

	@Inject
	PageManager manager;

	@Inject
	Style currentStyle;

	/**
	 * Method to perform Post Initialization Tasks.
	 */
	@PostConstruct
	public void activate()  {
		int depth = 2;
		ValueMap properties = null;
		//empty string is passed as relative path to get language toggle resource
		if(null != currentStyle && null != currentStyle.getDefiningResource("")) {
			properties = currentStyle.getDefiningResource("").getValueMap();
		}

		if (StringUtils.equalsIgnoreCase(currentPage.getAbsoluteParent(1).getName(), "prelaunch")) {
			depth++;
		}

		if (null != properties && properties.get("enabled", false) && currentPage.getDepth() > depth) {
			Iterator<Page> languages = currentPage.getAbsoluteParent(depth).listChildren(new PageFilter(request));
			Page page;
			Page localePage;

			final String currentLocale = Text.getAbsoluteParent(currentPage.getPath(), ++depth);

			while (languages.hasNext()) {
				page = languages.next();
				localePage = null != manager ? manager.getPage(
						StringUtils.replace(currentPage.getPath(), currentLocale, page.getPath())) : null;
				String concatLang = page.getName().toUpperCase() + "-" + PageUtil.getNavTitle(page);
				this.pages.put(retrievePagePath(localePage, page), concatLang);
				if (!isCurrentPage(currentLocale, page)) {
					this.isSelected.put(retrievePagePath(localePage, page), "selected");
				}
				else{
					this.isSelected.put(retrievePagePath(localePage, page), "");
				}
			}
		}
	}

	/**
	 * @param localePage    Page
	 * @param page 			Page
	 * @return boolean
	 */
	private String retrievePagePath(final Page localePage, final Page page) {
		if (null != localePage) {
			return localePage.getPath();
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


	/**
	 * Getter Method for pages.
	 *
	 * @return The list of pages.
	 */
	public final Map<String, String> getPages() {
		return pages;
	}

	/**
	 * Getter Method to identify the selected value of drop-down.
	 *
	 * @return The list of drop-down value.
	 */
	public Map<String, String> getIsSelected() {
		return isSelected;
	}
}
