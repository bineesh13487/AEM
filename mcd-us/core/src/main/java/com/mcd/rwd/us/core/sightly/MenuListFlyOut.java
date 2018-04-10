package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.MenuItem;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dipankar Gupta on 3/24/2016.
 */
@Component(name = "menulist-flyout", value = "Menu List Flyout",
		actions = { "text: Menu List Flyout", "-", "editannotate", "copymove", "delete" },
		disableTargeting = true,
		tabs = {@Tab( touchUINodeName = "links" , title = "Menu Details" )},
		group = "US RWD Navigation", path = "content",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MenuListFlyOut {

	@DialogField(name = "./link", fieldLabel = "Path")
	@PathField
	@Inject @Named("link")
	@Default(values = "")
	private String path;

	@DialogField(fieldLabel = "Title")
	@TextField
	@Inject @Default(values = "")
	private String title;

	@Inject
	PageManager pageManager;

	@Inject
	SlingHttpServletRequest request;

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuListFlyOut.class);

	private String link;

	private String pageTitle;

	private List<MenuItem> navList;

	private List<MenuItem> firstList;

	private List<MenuItem> secondList;

	/**
	 * Method to perform Post Initialization Tasks.
	 */
	@PostConstruct
	public void init() {

		String rootPagePath = this.path;

		Page resourcePage = pageManager.getContainingPage(request.getResource());
		this.pageTitle = resourcePage!=null ? (resourcePage.getTitle() !=null ? resourcePage.getTitle() : resourcePage.getPageTitle()) : ""; 
		LOGGER.debug("Menu List Fly Out - Menu page authored = "+rootPagePath);
		if (rootPagePath!=null) {
			Page page = pageManager.getPage(rootPagePath);

			if (page != null) {
				Iterator<Page> children = page.listChildren(new PageFilter(request));
				
				this.navList = new ArrayList<MenuItem>();
				while (children.hasNext()) {
	
					Page p = children.next();
					MenuItem menuItem = new MenuItem();
					menuItem.setTitle(PageUtil.getNavTitle(p));
					menuItem.setPath(LinkUtil.getHref(p.getPath()));
					this.navList.add(menuItem);
				}
					this.link = LinkUtil.getHref(rootPagePath);
					int size = this.navList.size();
					int index = size % 2 == 0 ? size / 2 : (size / 2) + 1;
					this.firstList = this.navList.subList(0, index);
					this.secondList = this.navList.subList(index, size);
			}
		}
	}
	public List<MenuItem> getFirstList() {
		return firstList;
	}

	public List<MenuItem> getSecondList() {
		return secondList;
	}

	public String getTitle() {
		return title;
	}

	public List<MenuItem> getNavList() {
		return navList;
	}
	
	public String getPageTitle() {
		return pageTitle;
	}
	
	public String getLink() {
		return link;
	}

}
