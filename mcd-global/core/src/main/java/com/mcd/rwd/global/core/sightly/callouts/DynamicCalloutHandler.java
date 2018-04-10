package com.mcd.rwd.global.core.sightly.callouts;

import com.google.gson.Gson;
import com.mcd.rwd.global.core.bean.FeatureCallout;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.constants.CalloutConstants;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Handles the processing of the component properties
 * for random and time-based callout types.
 *
 * @author HCL
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DynamicCalloutHandler { // extends McDUse {

	private Map<String, String> calloutMap;

	private int gridIdentifier;

	@Inject
	private Resource resource;

	@Inject @Named(CalloutConstants.PROPERTY_MARGIN_LEFT)
	@Default(intValues = 0)
	private int marginLeft;

	@Inject @Named(CalloutConstants.PROPERTY_MARGIN_TOP)
	@Default(intValues = 0)
	private int marginTop;

	@Inject @Named(CalloutConstants.PROPERTY_TYPE)
	private String type;

	@Inject @Named(CalloutConstants.PROPERTY_NUMBER)
	@Default(intValues = 1)
	private int calloutLimit;


	/* (non-Javadoc)
	 * @see com.adobe.cq.sightly.WCMUse#activate()
	 */
	@PostConstruct
	protected void activate() {
		this.calloutMap = generateCalloutMap();
		this.gridIdentifier =
				ApplicationConstants.TOTAL_GRIDS / resource.getValueMap().get(CalloutConstants.PROPERTY_NUMBER, 1);
	}

	/**
	 * Generates a Map from the properties configured
	 * for the feature callout.
	 *
	 * @return
	 */
	private Map<String, String> generateCalloutMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		//BumperUtil bumperUtil = new BumperUtil(getSiteConfigResource(ApplicationConstants.RES_BUMPER));
		BumperUtil bumperUtil = new BumperUtil(resource);
		Gson gson = new Gson();
		for (int i = 0; i < calloutLimit; i++) {
			ValueMap calloutProperties = ResourceUtil
					.getValueMap(resource, CalloutConstants.DYNAMIC_RESOURCE + (i + 1));
			if (null != calloutProperties) {
				//Set Feature Callout bean from the properties
				map.put(CalloutConstants.DYNAMIC_RESOURCE + (i + 1),
						gson.toJson(populateCalloutBean(calloutProperties, bumperUtil)));
			}
		}
		return map;
	}

	/**
	 * Process the random or time-based callout properties for a single
	 * callout and populate them in a callout bean.
	 *
	 * @param calloutMap
	 * @return
	 */
	private List<FeatureCallout> populateCalloutBean(ValueMap calloutMap, final BumperUtil bumperUtil) {
		List<FeatureCallout> calloutList = new LinkedList<FeatureCallout>();
		String[] imagePaths = calloutMap.get(CalloutConstants.PROPERTY_DYNAMIC_IMAGE_PATH, String[].class);
		String[] links = calloutMap.get(CalloutConstants.PROPERTY_DYNAMIC_LINK, String[].class);
		String[] overlayTexts = calloutMap.get(CalloutConstants.PROPERTY_DYNAMIC_TEXT, String[].class);
		String[] overlayPositions = calloutMap
				.get(CalloutConstants.PROPERTY_DYNAMIC_POSITION, String[].class);
		String[] overlayColors = calloutMap.get(CalloutConstants.PROPERTY_DYNAMIC_TEXTCOLOR, String[].class);
		String[] imageAlts = calloutMap.get(CalloutConstants.PROPERTY_DYNAMIC_ALTTEXT, String[].class);
		String[] startTimes = calloutMap.get(CalloutConstants.PROPERTY_DYNAMIC_STARTTIME, String[].class);
		String[] endTimes = calloutMap.get(CalloutConstants.PROPERTY_DYNAMIC_ENDTIME, String[].class);
		String[] targets = calloutMap.get(CalloutConstants.PROPERTY_DYNAMIC_TARGET, String[].class);
		//Iterate through the properties and set the callout bean
		for (int i = 0; i < imagePaths.length; i++) {
			FeatureCallout callout = new FeatureCallout();
			callout.setImagePath(imagePaths[i]);

			callout.setLink(bumperUtil.getLink(links[i], overlayTexts[i], false));
			callout.setOverlayPosition(overlayPositions[i]);
			callout.setOverlayColor(overlayColors[i]);
			callout.setImageAlt(imageAlts[i]);
			callout.setTarget(targets[i]);
			//Set time if callout type is not random
			if (!CalloutConstants.TYPE_RANDOM.equals(this.type)) {
				callout.setStartTime(startTimes[i]);
				callout.setEndTime(endTimes[i]);
			}
			calloutList.add(callout);
		}

		return calloutList;
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
	 * @return the calloutMap
	 */
	public Map<String, String> getCalloutMap() {
		return calloutMap;
	}

	/**
	 * @param calloutMap the calloutMap to set
	 */
	public void setCalloutMap(Map<String, String> calloutMap) {
		this.calloutMap = calloutMap;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
