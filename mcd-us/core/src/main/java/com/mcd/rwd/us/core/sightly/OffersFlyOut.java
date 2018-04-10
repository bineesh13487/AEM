package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.TextArea;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.foundation.Image;
import com.mcd.rwd.global.core.components.common.Text;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by Dipankar Gupta on 3/24/2016.
 */
@Component(name = "offers-flyout",value = "Offers Flyout", actions = { "text: Offers Flyout", "-", "editannotate", "copymove", "delete" },
		disableTargeting = true, group = "GWS-Global" , path="/content", isContainer = true)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OffersFlyOut{

	@Inject
	SlingHttpServletRequest request;

	@DialogField(fieldLabel = "Title")
	@DialogFieldSet(title = "Title", namePrefix = "title/")
	@ChildResource(name = "title")
	@Inject @Default(values = "text")
	private Text titleVal;

	@DialogField(fieldLabel = "Description" , fieldDescription = "Please provide the description for FAQ.")
	@TextArea
	@Inject
	String description;

	private String pageTitle = StringUtils.EMPTY;
	private String imageHref = StringUtils.EMPTY;
	private String title = StringUtils.EMPTY;

	/**
	 * Method to perform Post Initialization Tasks.
	 */
	@PostConstruct
	public final void activate() {
		Page resourcePage = ResourceUtil.getContainerPage(request.getResource());
		pageTitle = resourcePage!=null ? (resourcePage.getTitle() !=null ? resourcePage.getTitle() : resourcePage.getPageTitle()) : "";

		if(null!=titleVal) {
			title = titleVal.getText();
		}

		final Resource resource = request.getResource();
		final Resource imageResource = resource.getChild("image");
		if (imageResource != null) {
			final Image image = new Image(imageResource);
			if (image.hasContent()) {
				imageHref = resource.getPath() + "/image.img.png";
			}
		}
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
	
	public String getPageTitle() {
		return pageTitle;
	}

	public String getImageHref() {
		return imageHref;
	}
}
