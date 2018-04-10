package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.icfolson.aem.library.api.page.PageDecorator;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.MenuItem;
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
import java.util.List;

/**
 * Created by Dipankar Gupta on 3/24/2016.
 */
@Component(
		value = "Top Navigation",
		name = "topnavigation",
		path = "content",
		group = ".hidden"
)
@Model(adaptables = {SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TopNavigationHandler  {

	private static final Logger LOGGER = LoggerFactory.getLogger(TopNavigationHandler.class);

	@Inject
	PageManager pageManager;

	@Inject
	PageDecorator currentPage;

	@DesignAnnotation("top-navigation")
	Resource topNavResource;


	private static final String RESOURCE_PATH = "topnav-flyout";

	private List<MenuItem> navList;

	/**
	 * Method to perform Post Initialization Tasks.
	 */
	@PostConstruct
	public final void activate() {
		if (null != topNavResource) {
			ValueMap properties = topNavResource.getValueMap();
			if (properties != null) {
				LOGGER.debug("Properties available for Top Nav in Siteconfig.");
				String[] link = properties.get("links", String[].class);

				if (null != link && link.length > 0) {
					navList = new ArrayList<MenuItem>();
					for (int i = 0; i < link.length; i++) {
						addToList(link[i], pageManager);
					}
					LOGGER.debug("Total number of links received {}", navList.size());
				}
			}
		}
	}

	/**
	 * Adds the Resource to the list if the page exists.
	 *
	 * @param pagePath
	 * @param pageManager
	 */
	private final void addToList(String pagePath, PageManager pageManager) {

		String livePage = currentPage.getPath();

		if (livePage.contains("/prelaunch") && !(pagePath.contains("/prelaunch"))) {
			pagePath = pagePath.replace("/content","/content/prelaunch");
		}

		Page page = pageManager.getPage(pagePath);

		if (null != page && null != page.getContentResource(RESOURCE_PATH)) {
			MenuItem menuItem = new MenuItem();
			menuItem.setTitle(PageUtil.getNavTitle(page));
			menuItem.setPath(page.getContentResource(RESOURCE_PATH).getPath());
			menuItem.setURL(page.getPath() + ".html");
			navList.add(menuItem);
		}
	}

	/**
	 * Getter for Nav List.
	 *
	 * @return
	 */
	public List<MenuItem> getNavList() {
		return navList;
	}
}
