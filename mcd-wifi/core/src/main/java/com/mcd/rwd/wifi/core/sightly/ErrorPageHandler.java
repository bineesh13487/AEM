package com.mcd.rwd.wifi.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.editconfig.DropTarget;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.global.core.components.common.Image;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Component(
		name = "page-not-found",
		value = "Error Page",
		description = "404 page not found exception",
		actions = {"text: Error Page","-","edit","copymove","delete","insert"},
		disableTargeting = true,
		group = "McD-Wifi",
		dropTargets = {
				@DropTarget(propertyName = "./image/imagePath", accept = {"image/.*"}, groups = {"media"},
						nodeName = "image")
		},
		listeners = @Listener(name = "afteredit", value = "REFRESH_PAGE"),
		tabs = @Tab(title = "Error Page")
)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ErrorPageHandler {

	private String bgImage;

	@DialogField(fieldLabel = "Headline", fieldDescription = "Headline to display on wifi 404 error page.")
	@TextField
	@Inject @Default(values = "Oops!")
	private String headline;

	@DialogField(name = "./subheadline", fieldLabel = "Sub Headline", fieldDescription = "Sub headline " +
			"to display on wifi 404 error page.")
	@TextField
	@Inject @Named("subheadline")
	@Default(values = "Something Went Wrong.")
	private String subHeadline;

	@DialogField(fieldLabel = "Message", fieldDescription = "Message to display on wifi 404 error page.")
	@TextField
	@Inject @Default(values = "Please verify the page you are looking for")
	private String message;

	@DialogField(required = true)
	@DialogFieldSet(title = "Background Image", namePrefix = "image/")
	@ChildResource(name = "image")
	@Inject
	private Image image;

	@DialogField(name = "./hexcode", fieldLabel = "Hex Code", fieldDescription = "Please provide hex code " +
			"to change the text color of the error page. Note: Default Hex Code is : #ffffff (White)")
	@TextField
	@Inject @Named("hexcode")
	@Default(values = "#ffffff")
	private String hexCode;

	@PostConstruct
	protected void init() {
		this.bgImage = null != image ? image.getImagePath() : null;
	}

	public String getHeadline() {
		return headline;
	}

	public String getSubHeadline() {
		return subHeadline;
	}

	public String getMessage() {
		return message;
	}

	public String getBgImage() {
		return bgImage;
	}

	public String getHexCode() {
		return hexCode;
	}

	

}
