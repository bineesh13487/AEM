package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.*;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.mcd.rwd.global.core.components.common.Image;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.offers.Offer;
import com.mcd.rwd.us.core.service.impl.OfferPageFilter;
import com.mcd.rwd.us.core.utils.OfferUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

/**
 * Created by Rakesh.Balaiah on 07-06-2016.
 */
@Component(name = "offers",value = "Offers", actions = { "text: Offers", "-", "editannotate", "copymove", "delete" },
		disableTargeting = true, group = "GWS-Global" , path="/content",tabs = {@Tab(title = "basic"), @Tab(title = "image") , @Tab(title = "mobileimage") })
@Model(adaptables = {Resource.class ,SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OffersHandler {

	private static final Logger LOG = LoggerFactory.getLogger(OffersHandler.class);

	@Inject
	@Via("resource")
	PageManager pageManager;

	@Inject
	@Via("resource")
	SlingHttpServletRequest request;

	@Inject
	@Via("resource")
	Page currPage;

	@DialogField(fieldLabel = "Title", required = true , fieldDescription = "Title appearing on top of Hero Image.")
	@TextField
	@Inject
	String title;

	@DialogField(fieldLabel = "Description", required = true)
	@TextArea
	@Inject
	String description;

	@DialogField(fieldLabel = "Offer Pages Path", required = true , fieldDescription="Root Path under which all the offer pages would be searched and listed.")
	@PathField(rootPath="/content")
	@Inject
	String path;

	@DialogField(fieldLabel = "Limit", required = true , fieldDescription="No of rows to display initially.")
	@NumberField
	@Inject @Default(values = "1")
	int limit;

	@DialogField(fieldLabel = "Show App Badge?",fieldDescription="Enabling this would display the App Badges (Android &amp; iOS).")
	@CheckBox(text = "Show App Badge?")
	@Inject
	boolean showBadge;

	@DialogField(fieldLabel = "Validity Info Text", required = true , fieldDescription = "Text to display validity information. Use format Valid From {0} to {1}.{0} and {1} would be replaced by the start and end date respectively.")
	@TextField
	@Inject
	String validityText;

	@DialogField(fieldLabel = "Button Text", required = true , fieldDescription = "Label for the Load More Button.")
	@TextField
	@Inject
	String btnText;

	@DialogField(fieldLabel = "Hero Image", tab = 2)
	@DialogFieldSet(title = "Hero Image", namePrefix = "imageHref/")
	@ChildResource(name = "imageHref")
	@Inject
	private Image imageHref;

	@DialogField(fieldLabel = "Hero Image Mobile", tab = 3)
	@DialogFieldSet(title = "Hero Image Mobile", namePrefix = "mobileImageHref/")
	@ChildResource(name = "mobileImageHref")
	@Inject
	private Image mobileImageHref;

	private String bg;

	private String mobileBg;

	private List<Offer> offers;

	private Set<String> locations;

	private String id;

	@PostConstruct
	public void activate() throws Exception {
		if(null!=imageHref)
			bg = imageHref.getImagePath();
		if(null!=mobileBg)
			mobileBg = imageHref.getImagePath();
		id = "offers-" + ((int) Math.ceil(Math.random() * 100));
		locations = new HashSet<String>();
		if (StringUtils.isNotBlank(path) && pageManager.getPage(path) != null) {
			List<Resource> offersList = listOfferResources(pageManager.getPage(path));
			populateOffers(offersList, validityText);
		}
	}

	private List<Resource> listOfferResources(Page page) {
		List<Resource> list = new ArrayList<Resource>();
		Iterator<Page> pages = page.listChildren(new OfferPageFilter(), true);
		if (pages != null) {
			while (pages.hasNext()) {
				Page childPage = pages.next();
				Tag[] tags = childPage.getTags();
				if (tags != null && tags.length > 0) {
					LOG.debug("Tags available, making note of them");
					updateLocations(tags);
				} else {
					LOG.debug("Adding the offer to the list");
					list.add(childPage.getContentResource("promo"));
				}
			}
		}
		return list;
	}

	private void populateOffers(List<Resource> offersList, String validityText) {
		offers = new ArrayList<Offer>();
		int count = 1;
		String pageTitle = PageUtil.getPageNameForAnalytics(currPage, request);
		for (Resource resource : offersList) {
			Offer offer = OfferUtil.getOffer(resource, validityText);
			if (offer != null) {
				offer.setAnalytics("OfferBlock:"+ pageTitle + ":" + offer.getLink().getText() + ":" + count++);
				offers.add(offer);
			}
		}
	}

	private void updateLocations(Tag[] tags) {
		for(Tag tag : tags) {
			locations.add(tag.getTagID());
		}
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public List<Offer> getOffers() {
		return offers;
	}

	public String getBtnText() {
		return btnText;
	}

	public String getBg() {
		return bg;
	}

	public String getMobileBg() {
		return mobileBg;
	}

	public boolean isShowBadge() {
		return showBadge;
	}

	public String getLocationArray() {
		StringBuilder loc = new StringBuilder("[");

		for (String location: locations) {
			loc.append("'").append(location).append("',");
		}

		if (loc.length() > 1) {
			loc.deleteCharAt(loc.length() - 1);
		}

		return loc.append(']').toString();
	}

	public int getLimit() {
		return limit;
	}

	public String getId() { return id; }
}
