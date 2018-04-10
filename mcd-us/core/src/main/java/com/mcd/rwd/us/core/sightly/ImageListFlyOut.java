package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Design;
import com.icfolson.aem.multicompositeaddon.widget.MultiCompositeField;
import com.mcd.rwd.global.core.bean.Link;
import com.mcd.rwd.global.core.bean.html.Image;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.ResourceUtil;
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
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dipankar Gupta on 3/24/2016.
 */
@Component(name = "imagelist-flyout", value = "Image List Flyout",
		description = "Displays multiple images with different layout options.",
		disableTargeting = true,
		actions = { "text: Image List Flyout", "-", "editannotate", "copymove", "delete" },
		group = "US RWD Navigation", path = "content",
		tabs = {@Tab(touchUINodeName = "basic", title = "Links"),
				@Tab(touchUINodeName = "advanced", title = "Advanced")},
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE")
		})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageListFlyOut {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageListFlyOut.class);


	@DialogField(fieldLabel = "Image Details",
			additionalProperties = @Property(name = "class", value = "image-details"))
	@MultiCompositeField
	@Inject
	private List<Image> imageList;


	@DialogField(
			fieldLabel = "Display list in 2 rows after container width",additionalProperties =
			{@Property(name = "width", value = "100"), @Property(name = "value", value = "true")},
			name = "./enableMultiRow",
			tab = 2
	)
	@CheckBox(text = "Display list in 2 rows after container width")
	@Inject @Named("enableMultiRow")
	private boolean enableMultiRow;

	private String pageTitle;

	@Inject
	Resource currentResource;

	@Inject
	Design currentDesign;

	@DesignAnnotation("top-navigation")
	Resource topNavResource;

	/**
	 * Method to perform Post Initialization Tasks.
	 */
	@PostConstruct
	public final void activate() {

		ValueMap topNavProperties = topNavResource.getValueMap();

		if (currentResource != null) {
			if(currentResource.hasChildren()){
				Resource imageRes = currentResource.getChild("imageList");
				Iterator<Resource> resourceIterator = imageRes.listChildren();
				imageList = new ArrayList<Image>();
				while(resourceIterator.hasNext()){
					Resource itemRes = resourceIterator.next();
					ValueMap properties = itemRes.adaptTo(ValueMap.class);
					Resource contentResource = currentDesign.getContentResource();
					if(null!=properties && null != contentResource) {
							Image image = new Image();
							BumperUtil bumperUtil = new BumperUtil(contentResource.getChild(ApplicationConstants.RES_BUMPER));
							Page resourcePage = ResourceUtil.getContainerPage(currentResource);
							pageTitle = resourcePage!=null ? (resourcePage.getTitle() !=null ? resourcePage.getTitle() : resourcePage.getPageTitle()) : "";
								image.setLocalUrl(properties.get("link") != null ? isLocalUrl(topNavProperties, properties.get("link").toString()) : false);
								if(null != properties.get("link") && null != properties.get("title")){
									Link bumperLink = bumperUtil.getLink(properties.get("link").toString(), properties.get("title").toString(), false);
									image.setBumperLink(bumperLink);
								}

								if(itemRes.hasChildren()){
									Resource src = itemRes.getChild("src");
									image.setSrc(src != null ? src.getValueMap().get("fileReference", String.class) : StringUtils.EMPTY);
								} else if (properties.get("image") != null) {
									image.setSrc(properties.get("image", String.class));
								}
								image.setSubTitle(properties.get("subTitle", StringUtils.EMPTY));
								image.setTag(properties.get("tag") != null ? getTagString(properties.get("tag", String[].class), itemRes) : StringUtils.EMPTY);
								imageList.add(image);

					}
				}

			}

		}
	}

	private boolean isLocalUrl(ValueMap topNavValueMap, String link) {
		return (topNavValueMap!=null && topNavValueMap.get("localurl",String.class)!=null) ? topNavValueMap.get("localurl",String.class).equals(link) : false;
	}

	private String getTagString(String[] arr , Resource tagRes) {
		TagManager tm = tagRes.getResourceResolver().adaptTo(TagManager.class);
		String tagString = "";
		if (arr!=null) {
			for (int k=0; k<arr.length; k++) {
				Tag t = tm.resolve(arr[k]);
				if (t!=null) {
					tagString = tagString + t.getName() + ",";
				}
			}
			if(tagString.endsWith(",")){
				tagString = tagString.substring(0, tagString.length() - 1);
			}
			LOGGER.debug("image lsit tagstring -- "+tagString);
		}
		return tagString;
	}

	public boolean isEnableMultiRow() {
		return enableMultiRow;
	}

	public List<Image> getImageList() {
		return imageList;
	}
	
	public String getPageTitle() {
		return pageTitle;
	}


}
