package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Style;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.NavItem;
import com.mcd.rwd.us.core.models.FooterNav;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by karan-k on 3/15/2016.
 * Modified by rakesh-b on 4/12/2016.
 */

@Component(
		name = "footernav",
		value = "Footer Navigation",
		path = "content",
		editConfig = false,
		group = ".hidden"
)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterNavigationHandler {

	private static final Logger LOG = LoggerFactory.getLogger(FooterNavigationHandler.class);

	private static final int CSS_COLS = 12;

	private static final int MAX_COLS = 6;

	@Inject
	private boolean isMobile;

	private List<NavItem> navList;

	private int numberOfCols;

	private int navListSize;

	@Self
	private SlingHttpServletRequest request;

	@Inject
	Page currentPage;

	@Inject
	PageManager pageManager;

	@Inject
	private Design currentDesign;

	@DesignAnnotation(value = "footernav")
	Resource footerNav;

	@Inject
	Style currentStyle;

	/**
	 * Method to perform Post Initialization Tasks.
	 */
	@PostConstruct
	public final void activate() {

		LOG.debug("Is mobile script ? {}", this.isMobile);

		PageFilter pageFilter = new PageFilter(request);
		Page siteRoot = currentPage.getAbsoluteParent(1);
		Resource footerNavResource = null;
		if(null != currentDesign) {
			Resource resource = currentDesign.getContentResource();
			footerNavResource = resource.getChild("footernav");
		}

		//Resource footerNavResource = getSiteConfigResource("footernav");

		if (null != footerNavResource) {
			LOG.debug("Footer nav config available. Processing further.");
			FooterNav footerNav = footerNavResource.adaptTo(FooterNav.class);

			if (!FooterNav.AUTO.equals(footerNav.getListType())) {
				List<String> customList = footerNav.getCustomList();
				LOG.debug("Received list {}.", customList);
				if (null != customList && !customList.isEmpty()) {
					LOG.debug("Processing using custom list configured.");
					this.navList = createNavList(customList, pageFilter);
				}
			} else {
				//Use the rootPath of the site in case not configured by the author.
				String rootPath = footerNav.getRootPath() != null ? footerNav.getRootPath() : siteRoot.getPath();
				LOG.debug("Processing using the root path {} configured.", rootPath);
				if (currentPage.getPath().contains("/prelaunch") && !(rootPath.contains("/prelaunch"))) {
					rootPath = rootPath.replace("/content","/content/prelaunch");
				}
				Page rootPage = pageManager.getPage(rootPath);
				this.navList = createNavList(rootPage, pageFilter);
			}
		}

		if (null == this.navList) {
			LOG.debug("List not generated. Generating list using the site home {}", siteRoot);
			this.navList = createNavList(siteRoot, pageFilter);
		}
		this.navListSize = this.navList.size();
	}

	/**
	 * Creates the Navigation List for the Root Page provided.
	 *
	 * @param page       The parent path.
	 * @param pageFilter Filter to use.
	 * @return List<NavItem>
	 */
	private List<NavItem> createNavList(final Page page, final PageFilter pageFilter) {
		List<NavItem> navListItems = new ArrayList<NavItem>();
		Iterator<Page> firstChildren = page.listChildren(pageFilter, false);

		int cols = 0;
		while (firstChildren.hasNext() && cols < MAX_COLS) {
			Page firstChild = firstChildren.next();
			NavItem firstLevelItem = createNavItem(firstChild);

			if (!isMobile) {
				firstLevelItem.setNavList(createSubList(firstChild, pageFilter));
			}

			navListItems.add(firstLevelItem);
			cols++;
		}
		if(cols>0)
		numberOfCols = CSS_COLS / cols;
		return navListItems;
	}

	/**
	 * Creates a list of Navigation Items using Customized Links configured by the author.
	 *
	 * @param links  Array of custom links.
	 * @param filter Filter to use.
	 * @return List<NavItem>
	 */
	private List<NavItem> createNavList(final List<String> links, final PageFilter filter) {
		List<NavItem> navCustomList = new ArrayList<NavItem>();
		String livePage = currentPage.getPath();
		int cols = 0;
		Iterator<String> itr = links.iterator();
		//PageManager pageManager = getPageManager();

		while (itr.hasNext() && cols < links.size()) {
			String link = itr.next();
			if (livePage.contains("/prelaunch") && !(link.contains("/prelaunch"))) {
				link = link.replace("/content","/content/prelaunch");
			}
			Page mainPage = pageManager.getPage(link);
			if (mainPage != null && !mainPage.isHideInNav()) {
				NavItem item = createNavItem(mainPage);

				if (!isMobile) {
					item.setNavList(createSubList(mainPage, filter));
				}

				navCustomList.add(item);
				cols++;
			}
		}

		if(cols>0)
		numberOfCols = CSS_COLS / cols;
		return navCustomList;
	}

	/**
	 * Generates the Second Level Navigation Items.
	 *
	 * @param page   The first level parent.
	 * @param filter Filter to use.
	 * @return List<NavItem>
	 */
	private List<NavItem> createSubList(final Page page, final PageFilter filter) {
		List<NavItem> subList = new ArrayList<NavItem>();
		Iterator<Page> subChildren = page.listChildren(filter);
		while (subChildren.hasNext()) {
			Page subChild = subChildren.next();
			NavItem secondLevelItem = createNavItem(subChild);
			subList.add(secondLevelItem);
		}
		return subList;
	}

	/**
	 * Getter Method for navList.
	 *
	 * @return List<NavItem>
	 */
	public final List<NavItem> getNavList() {
		return navList;
	}

	/**
	 * Creates a new NavItem and returns it.
	 *
	 * @param page - The Page from which the NavItem needs to be created
	 * @return NavItem
	 */
	private NavItem createNavItem(final Page page) {
		NavItem item = new NavItem();
		item.setPath(LinkUtil.getHref(page.getPath()));
		item.setTitle(PageUtil.getNavTitle(page));
		return item;
	}

	public int getNumberOfCols() {
		return numberOfCols;
	}

	public int getNavListSize() {
		return navListSize;
	}
}
