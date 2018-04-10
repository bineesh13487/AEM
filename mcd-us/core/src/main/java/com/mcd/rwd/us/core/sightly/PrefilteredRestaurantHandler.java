package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Tab;
import com.day.cq.wcm.api.designer.Design;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.Switch;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcd.rwd.global.core.bean.Link;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.sightly.McDUse;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Component(name = "prefiltered-restaurant",value = "Prefiltered Restaurant", actions = { "text: Pre-Filtered Restaurant", "-", "editannotate", "copymove", "delete" },
		disableTargeting = true, group = "GWS-Global" , path="/content")
@Model(adaptables = {Resource.class ,SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PrefilteredRestaurantHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PrefilteredRestaurantHandler.class);

	@DialogField(fieldDescription="To provide page path of Restaurant Locator Page", fieldLabel="Restaurant Locator page path")
	@PathField
	@Inject
	String restaurantpagepath;

	@DialogField(fieldLabel="Filter/Service" , fieldDescription="To select the filter to set in pre-filtered Restaurant Locator")
	@Selection(optionsUrl = "$PATH.restaurantSelectedAttributes.json", dataSource = "mcd-us/dataSource/prefiltered-restaurant", type = Selection.SELECT)
	@Inject
	private String restaurantservices;

	@DialogField( fieldDescription="Check to show CTA button as a link", fieldLabel="Check this to show link instead of CTA button")
	@Switch
	@Inject
	boolean linkorbutton;

	@DialogField(fieldDescription="Select the position to decide placement of CTA button", fieldLabel="Position of CTA button",defaultValue="left")
	@Selection(options = {
			@Option(text = "Left", value = "left"),
			@Option(text = "Center", value = "center"),
			@Option(text = "Right", value = "right")

	}, type = Selection.SELECT)
	@Inject @Default(values = "center")
	String linkorButtonAllign;

	@DialogField(fieldDescription="To provide text for CTA button / Links",fieldLabel="CTA button/Link Text")
	@TextField
	@Inject
	String ctabuttonname;

	@DialogField(fieldDescription="This will change text color of link or button.", fieldLabel="CTA button/Link Text Color")
	@ColorPicker
	@Inject @Default(values = "FFFFFF")
	String buttonTextColor;

	@DialogField(fieldDescription="This will change the background color of button.", fieldLabel="CTA button Background Color")
	@ColorPicker
	@Inject @Default(values = "BE0C0C")
	String btnbgcolor;

	@DialogField( fieldDescription="Check to show CTA button as a link", fieldLabel="Check this to show link instead of CTA button")
	@Switch
	@Inject
	String target;

	@Inject
	Design currentDesign;

	private String serviceButtonName;
	private String restaurantLink;
	private boolean linkOrButton;
	private Link restaurantPagePath;

	@PostConstruct
	public void activate() throws Exception {
		LOGGER.debug("PrefilteredRestaurantHandler Activate Method Called..");
		 Boolean targetObj = new Boolean(false);
		 if(target != null)
		 {
			 targetObj=true; 
		 }
		 else
		 {
			 targetObj=false; 
		 }
		 if(null != restaurantpagepath){
			 String pagePathLink = LinkUtil.getHref(restaurantpagepath)+"?"+"service="+restaurantservices;
			 Resource restaurantResource = null;
			 if(currentDesign != null){
				 Resource contentResource = currentDesign.getContentResource();
				 restaurantResource = contentResource.getChild(ApplicationConstants.RES_BUMPER);
				 BumperUtil bumperUtil = new BumperUtil(restaurantResource);
				 restaurantPagePath = bumperUtil.getLink(pagePathLink,ctabuttonname,targetObj);
			 }


			 serviceButtonName= ctabuttonname;
			 int dot1 = restaurantPagePath.getHref().indexOf(".");
			 int dot2 = restaurantPagePath.getHref().indexOf(".", dot1 + 1);
			 restaurantLink = restaurantPagePath.getHref().substring(0, dot2);
		 }

	}

	public String getBtnbgcolor() {
		return btnbgcolor;
	}
	public boolean isLinkOrButton() {
		return linkOrButton;
	}
	public String getLinkorButtonAllign() {
		return linkorButtonAllign;
	}
	public String getButtonTextColor() {
		return buttonTextColor;
	}
	public String getRestaurantLink() {
		return restaurantLink;
	}

	public Link getRestaurantPagePath() {
		return restaurantPagePath;
	}

	public String getServiceButtonName() {
		return serviceButtonName;
	}

	public String getTarget() {
		return target;
	}

}
