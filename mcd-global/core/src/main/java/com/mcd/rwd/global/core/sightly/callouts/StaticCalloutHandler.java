/**
 *
 */
package com.mcd.rwd.global.core.sightly.callouts;

import com.mcd.rwd.global.core.bean.FeatureCallout;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.constants.CalloutConstants;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.LinkedList;
import java.util.List;

/**
 * @author HCL
 */

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class StaticCalloutHandler {

	private List<FeatureCallout> calloutList;

	@Inject @Named(CalloutConstants.PROPERTY_MARGIN_LEFT)
	@Default(intValues = 0)
	private int marginLeft;

	@Inject @Named(CalloutConstants.PROPERTY_MARGIN_TOP)
	@Default(intValues = 0)
	private int marginTop;

	@Inject @Named(CalloutConstants.PROPERTY_NUMBER)
	@Default(intValues = 1)
	private int calloutLimit;

	@DesignAnnotation(ApplicationConstants.RES_BUMPER)
	Resource bumperRes;

	@Inject
	Resource resource;

	private int gridIdentifier;

	/* (non-Javadoc)
	 * @see com.adobe.cq.sightly.WCMUse#activate()
	 */
	@PostConstruct
	public void activate() {
		this.calloutList = generateCalloutList();
		this.gridIdentifier =
				ApplicationConstants.TOTAL_GRIDS / calloutLimit;
	}

	/**
	 * Retrieves the required information through component properties
	 * and returns the list of callouts.
	 *
	 * @return
	 */
	public List<FeatureCallout> generateCalloutList() {
		BumperUtil bumperUtil = new BumperUtil(bumperRes);
		List<FeatureCallout> featureCalloutList = new LinkedList<FeatureCallout>();
		for (int i = 0; i < calloutLimit; i++) {
			ValueMap calloutProperties = ResourceUtil
					.getValueMap(resource, CalloutConstants.TYPE_STATIC + (i + 1));
			if (null != calloutProperties) {
				//Set Feature Callout bean from the properties
				FeatureCallout callout = new FeatureCallout();
				callout.setImagePath(
						LinkUtil.getImageHref(resource, CalloutConstants.TYPE_STATIC + (i + 1)));
				String link = calloutProperties.get(CalloutConstants.PROPERTY_STATIC_LINK, StringUtils.EMPTY);
				callout.setLink(bumperUtil.getLink(link,
						calloutProperties.get(CalloutConstants.PROPERTY_STATIC_TEXT, StringUtils.EMPTY),
						false));
				callout.setOverlayPosition(
						calloutProperties.get(CalloutConstants.PROPERTY_STATIC_POSITION, StringUtils.EMPTY));
				callout.setImageAlt(
						calloutProperties.get(CalloutConstants.PROPERTY_STATIC_ALTTEXT, StringUtils.EMPTY));
				callout.setOverlayColor(
						calloutProperties.get(CalloutConstants.PROPERTY_STATIC_TEXTCOLOR, StringUtils.EMPTY));
				callout.setTarget(
						calloutProperties.get(CalloutConstants.PROPERTY_STATIC_TARGET, StringUtils.EMPTY));
				featureCalloutList.add(callout);
			}
		}
		return featureCalloutList;
	}

	/**
	 * @return the calloutList
	 */
	public List<FeatureCallout> getCalloutList() {
		return calloutList;
	}

	/**
	 * @param calloutList the calloutList to set
	 */
	public void setCalloutList(List<FeatureCallout> calloutList) {
		this.calloutList = calloutList;
	}

	/**
	 * @return the marginLeft
	 */
	public int getMarginLeft() {
		return marginLeft;
	}

	/**
	 * @param marginLeft the marginLeft to set
	 */
	public void setMarginLeft(int marginLeft) {
		this.marginLeft = marginLeft;
	}

	/**
	 * @return the marginTop
	 */
	public int getMarginTop() {
		return marginTop;
	}

	/**
	 * @param marginTop the marginTop to set
	 */
	public void setMarginTop(int marginTop) {
		this.marginTop = marginTop;
	}

	/**
	 * @return the gridIdentifier
	 */
	public int getGridIdentifier() {
		return gridIdentifier;
	}

	/**
	 * @param gridIdentifier the gridIdentifier to set
	 */
	public void setGridIdentifier(int gridIdentifier) {
		this.gridIdentifier = gridIdentifier;
	}

}
