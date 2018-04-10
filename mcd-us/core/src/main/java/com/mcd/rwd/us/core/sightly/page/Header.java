package com.mcd.rwd.us.core.sightly.page;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;
import com.mcd.rwd.global.core.bean.Link;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.MultiFieldPanelUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.constants.ADAConstants;
import com.mcd.rwd.us.core.constants.RLConstants;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

/**
 * Created by Rakesh.Balaiah on 12-04-2016.
 */

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Header {
	
	//@Reference
	//private transient McdWebServicesConfig mcdWebServicesConfig;

	private boolean languageToggleEnabled;
	
	private String topNavigationAria = StringUtils.EMPTY;

	private boolean myAccountEnabled;

	private String myAccountText;

	private static final String FILTER_NAME_KEY = "filterName";

	private String filterString;

	private static final String RL_SITE_CONFIG = "rl";
	
	private static final String ADA_TEXT_SITE_CONFIG = "adagenerictexts";

	private String radiusSelected;

	private String restaurantSearchText;

	private String submit = "false";

	private String locate = "false";

	private List<String> filterSelected = new ArrayList<String>();

	private List<Link> headerLinks;

    private Link joinLink;
    
    private String pageNameForAnalytics = StringUtils.EMPTY;

	/**
	 * default logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Header.class);

	private boolean isRlPage = false;

	private boolean joinLinkEnabled;
	
	private boolean locateLinkEnabled;

	private String pageName ;

	private String [] monthOptionValue = new String[] {"January","February","March","April","May","June","July","August","September","October","November","December"};


    @Inject
    SlingHttpServletRequest request;

    @Inject
    Page currentPage;

    @Inject
    Style currentStyle;

    @DesignAnnotation(value = "languagetoggle")
    Resource languagetoggleRes;

    @DesignAnnotation(value = "top-navigation")
    Resource topNavigationRes;

    @DesignAnnotation(value = "joinlinktitle")
    Resource joinlinktitleRes;

    @DesignAnnotation(value = "myaccount")
    Resource myAccountResource;

    @DesignAnnotation(value = "headerlinks")
    Resource headerLinkResource;

    @DesignAnnotation(value = "rl")
    Resource rlResource;

    @DesignAnnotation(value = ADA_TEXT_SITE_CONFIG)
    Resource adagenerictextsRes;

    @DesignAnnotation(value = ApplicationConstants.RES_BUMPER)
    Resource bumperRes;

    @OSGiService
    McdWebServicesConfig mcdWebServicesConfig;

    @PostConstruct
    public void activate() throws Exception {

        this.pageName = PageUtil.getPageNameForAnalytics(currentPage);
        this.pageNameForAnalytics = PageUtil.getPageNameForAnalytics(currentPage);

        ValueMap languageToggleProps = null != languagetoggleRes ? languagetoggleRes.getValueMap() : null;

		if (null != languageToggleProps) {
			this.languageToggleEnabled = languageToggleProps.get("enabled", false);
		}
		
		ValueMap topNavigationProps = null != topNavigationRes ? topNavigationRes.getValueMap() : null;

		if (null != topNavigationProps) {
			this.topNavigationAria = topNavigationProps.get("aria", "");
		}

		ValueMap joinLinkProps = null != joinlinktitleRes ? joinlinktitleRes.getValueMap() : null;

        joinLink = new Link();
		if (null != joinLinkProps) {
			
			this.joinLinkEnabled = joinLinkProps.get("enabled", false);
			if(this.joinLinkEnabled)
			{
            joinLink.setHref(LinkUtil.getHref(joinLinkProps.get("pagePath", "")));
            joinLink.setText(joinLinkProps.get("text","Join"));
		}

		}

		ValueMap myAccountProps = myAccountResource == null ? null : myAccountResource.getValueMap();

		if (null != myAccountProps) {
			this.myAccountText = myAccountProps.get("headerTitle", "MyAccount");
			this.myAccountEnabled = myAccountProps.get("enabled", false);
		} else {
			this.myAccountText = "MyAccount";
		}
		if (null != headerLinkResource) {
			LOGGER.debug("Header Links resource available {}", headerLinkResource.getPath());
			populateHeaderLinks(headerLinkResource);
		}


		String uri = request.getRequestURI();
		//HttpServletRequest request = getRequest();
		Map<String, String> requestMap = request.getParameterMap();
		Set<String> requestNames = requestMap.keySet();
		for (String requestName : requestNames) {
			if (StringUtils.contains(requestName, "checkbox_")) {
				this.filterSelected.add(StringUtils.substringAfter(requestName, "checkbox_"));
			}
		}
		if (request.getParameter(RLConstants.SUBMIT) != null
				&& Boolean.parseBoolean(request.getParameter(RLConstants.SUBMIT))) {

			this.submit = request.getParameter(RLConstants.SUBMIT);

			if (request.getParameter("searchText") != null) {
				if(request.getRequestURL().toString().contains("/content/")){
					this.restaurantSearchText = new String(request.getParameter("searchText").getBytes("8859_1"), "UTF-8");
				}else{
					this.restaurantSearchText=new java.net.URLDecoder().decode(request.getParameter("searchText"), "UTF-8");
					LOGGER.debug("In Header.java and String for Resturant Search decoding second  time"+this.restaurantSearchText);
				}
			}
			if (request.getParameter(RLConstants.RADIUSSELECTED) != null) {
				this.radiusSelected = request.getParameter(RLConstants.RADIUSSELECTED);
			}
		} else if (request.getParameter(RLConstants.LOCATE) != null
				&& Boolean.parseBoolean(request.getParameter(RLConstants.LOCATE))) {
			this.locate = request.getParameter(RLConstants.LOCATE);
			if (request.getParameter(RLConstants.RADIUSSELECTED) != null) {
				this.radiusSelected = request.getParameter(RLConstants.RADIUSSELECTED);
			}
		}

		//ValueMap restLocPath = getSiteConfig("rl");
		ValueMap restLocPath = null != rlResource ? rlResource.getValueMap() : null;

		if (restLocPath != null) {
			this.locateLinkEnabled = restLocPath.get("enabled", false);
			String rlPath = restLocPath.get("restLocPath", "") + ".html";
			if (rlPath.contains(uri)) {
				this.isRlPage = true;
			}
		}

		initializeFilters();
	}

	private void initializeFilters() {

		filterString = "";
		if (rlResource != null) {
			List<Map<String, String>> restaurantFilters = MultiFieldPanelUtil
					.getMultiFieldPanelValues(rlResource, RLConstants.RESTAURANTFILTERS);
			if (restaurantFilters != null && !restaurantFilters.isEmpty()) {
				Iterator<Map<String, String>> itr = restaurantFilters.iterator();
				while (itr.hasNext()) {
					Map<String, String> item = itr.next();
					String filterMarketingName = item.get(RLConstants.FILTERMARKETINGNAME);
					String filterMarketCss = item.get(RLConstants.FILTERMARKETINGCSS);
					String filterName = item.get(FILTER_NAME_KEY);
					Boolean serviceAsFilter = Boolean.valueOf(item.get(RLConstants.SERVICEASFILTER));
					String filterKey = (filterMarketingName != null
							&& filterMarketingName.trim().length() > 0) ? filterMarketingName : filterName;
					setFilterString(filterKey + ":" + filterName + "dividecss" + filterMarketCss +":"+ serviceAsFilter);

				}
			} else {
				LOGGER.info("No restaurant filters configured in the restaurant locator page");
			}
		}
	}

	public String getPageTitle() {
		return this.pageName ;
	}
	private String addDelimiter(String sourceString) {
		String returnSourceString = sourceString;
		if (StringUtils.isNotEmpty(sourceString)) {
			returnSourceString += ",";
		}
		return returnSourceString;
	}

	private void setFilterString(String service) {
		this.filterString = addDelimiter(filterString);
		this.filterString = this.filterString + service;
	}

	private String getDistanceUnit(String distanceUnit,ValueMap dialogProperties) {
		if(distanceUnit.equalsIgnoreCase("Kilometer")) {
		return dialogProperties.get(RLConstants.KILOLOCAL, StringUtils.EMPTY);
		} else {
			return dialogProperties.get(RLConstants.MILESLOCAL, StringUtils.EMPTY);
		}

	}
	/**
	 * Generates the JSON string containing the RL Configurations.
	 * @return
	 */
	public String initializeRLJsonLabels() {
		JSONObject locatorJson = new JSONObject();
		String jsonData = StringUtils.EMPTY;
		try {

			//ValueMap dialogProperties = getSiteConfig(RL_SITE_CONFIG);
			ValueMap dialogProperties = null != rlResource ? rlResource.getValueMap() : null;
			if (dialogProperties != null) {
				String[] radiusProp = dialogProperties.get("radius", ArrayUtils.EMPTY_STRING_ARRAY);
				List<String> radiusList = new ArrayList<String>();
				if (null != radiusProp) {
					for (int index = 0; index < radiusProp.length; index++) {

						radiusList.add(radiusProp[index]);
					}
				}
				locatorJson.put(RLConstants.PAGENAME,
						this.pageName);

				locatorJson.put(RLConstants.OPERATORLEGALTEXT,
						dialogProperties.get(RLConstants.OPERATORLEGALTEXT, StringUtils.EMPTY));

				locatorJson.put(RLConstants.OPERATORFRANCHISETEXT,
						dialogProperties.get(RLConstants.OPERATORFRANCHISETEXT, StringUtils.EMPTY));


				locatorJson.put(RLConstants.MCARRIERS,
						dialogProperties.get(RLConstants.MCARRIERS, StringUtils.EMPTY));

				locatorJson.put(RLConstants.MACARRIERS,
						dialogProperties.get(RLConstants.MACARRIERS, StringUtils.EMPTY));

				locatorJson.put(RLConstants.NOCARRIER,
						dialogProperties.get(RLConstants.NOCARRIER, StringUtils.EMPTY));
				
				locatorJson.put(RLConstants.SHOWJOBSECTION,
						dialogProperties.get(RLConstants.SHOWJOBSECTION, StringUtils.EMPTY));

				locatorJson.put(RLConstants.ISAPPLYFORJOB,
						dialogProperties.get(RLConstants.ISAPPLYFORJOB, StringUtils.EMPTY));

				locatorJson.put(RLConstants.CARRIERSHEADING,
						dialogProperties.get(RLConstants.CARRIERSHEADING, StringUtils.EMPTY));



				/*locatorJson.put(RLConstants.LOCATIONDISAMBIGUITY,
						dialogProperties.get(RLConstants.LOCATIONDISAMBIGUITY, StringUtils.EMPTY));*/

				locatorJson.put(RLConstants.SELECTLOCATIONBELOW,
						dialogProperties.get(RLConstants.SELECTLOCATIONBELOW, StringUtils.EMPTY));


				locatorJson.put(RLConstants.RLMAPVIEW,
						dialogProperties.get(RLConstants.RLMAPVIEW, StringUtils.EMPTY));

				locatorJson.put(RLConstants.RLLISTVIEW,
						dialogProperties.get(RLConstants.RLLISTVIEW, StringUtils.EMPTY));
				locatorJson.put(RLConstants.HOURS24TEXT,
						dialogProperties.get(RLConstants.HOURS24TEXT, StringUtils.EMPTY));
				locatorJson.put(RLConstants.HOURS24TEXTFORARIALABEL,
						dialogProperties.get(RLConstants.HOURS24TEXTFORARIALABEL, "open 24 hours"));
				locatorJson.put(RLConstants.RESTAURANTNOTAVAILABLE,
						dialogProperties.get(RLConstants.RESTAURANTNOTAVAILABLE, StringUtils.EMPTY));
				locatorJson.put(RLConstants.LOCATIONPERMISSIONDENIEDTEXT,
						dialogProperties.get(RLConstants.LOCATIONPERMISSIONDENIEDTEXT, StringUtils.EMPTY));
				locatorJson.put(RLConstants.LOCATIONUNAVAIALBELTEXT,
						dialogProperties.get(RLConstants.LOCATIONUNAVAIALBELTEXT, StringUtils.EMPTY));
				locatorJson.put(RLConstants.LOCATIONUNKNOWNERRORRTEXT,
						dialogProperties.get(RLConstants.LOCATIONUNKNOWNERRORRTEXT, StringUtils.EMPTY));
				locatorJson.put(RLConstants.LOCATIONUNAVAIALBELTEXT,
						dialogProperties.get(RLConstants.LOCATIONUNAVAIALBELTEXT, StringUtils.EMPTY));
				locatorJson.put(RLConstants.MCDNAME,
						dialogProperties.get(RLConstants.MCDNAME, StringUtils.EMPTY));
				locatorJson.put(RLConstants.ON, dialogProperties.get(RLConstants.ON, StringUtils.EMPTY));

				locatorJson.put(RLConstants.LOCNEARLABEL,
						dialogProperties.get(RLConstants.LOCNEARLABEL, StringUtils.EMPTY));
				locatorJson.put(RLConstants.PREFEREDLOCALLABEL,
						dialogProperties.get(RLConstants.PREFEREDLOCALLABEL, StringUtils.EMPTY));
				locatorJson.put(RLConstants.NEARESTLOCLABEL,
						dialogProperties.get(RLConstants.NEARESTLOCLABEL, StringUtils.EMPTY));
				locatorJson.put(RLConstants.LOCATELABEL,
						dialogProperties.get(RLConstants.LOCATELABEL, StringUtils.EMPTY));

				locatorJson.put(RLConstants.APPLY_FOR_JOB,
						dialogProperties.get(RLConstants.APPLY_FOR_JOB, StringUtils.EMPTY));
				locatorJson.put(RLConstants.GEOCOUNTRY,
						dialogProperties.get(RLConstants.GEOCOUNTRY, StringUtils.EMPTY));
				locatorJson.put(RLConstants.FILTERSELECTED, getFilterSelected());
				locatorJson.put(RLConstants.SUBMIT, getSubmit());
				locatorJson.put(RLConstants.LOCATE, getLocate());
				locatorJson.put(RLConstants.RESTAURANTSEARCHTEXT, getRestaurantSearchText());
				locatorJson.put(RLConstants.RADIUSSELECTED, getRadiusSelected());
				locatorJson.put(RLConstants.ISRLPAGE, isRlPage());
				locatorJson.put(RLConstants.LOADMORE,
						dialogProperties.get(RLConstants.LOADMORE, StringUtils.EMPTY));
				locatorJson.put(RLConstants.RESULTITERATE,
						dialogProperties.get(RLConstants.RESULTITERATE, StringUtils.EMPTY));
				locatorJson.put(RLConstants.RESULTMESSAGE,
						dialogProperties.get(RLConstants.RESULTMESSAGE, StringUtils.EMPTY));
				locatorJson.put(RLConstants.COUNTRY,
						dialogProperties.get(RLConstants.COUNTRY, StringUtils.EMPTY));
				locatorJson.put(RLConstants.LANGUAGE,
						dialogProperties.get(RLConstants.LANGUAGE, StringUtils.EMPTY));
				/*locatorJson.put(RLConstants.NORESULTFOUNDTEXT,
						dialogProperties.get(RLConstants.NORESULTFOUNDTEXT, StringUtils.EMPTY));*/
				locatorJson.put(RLConstants.SHOWCLOSED,
						dialogProperties.get(RLConstants.SHOWCLOSED, StringUtils.EMPTY));
				locatorJson.put(RLConstants.MAXRESULTS, dialogProperties.get(RLConstants.MAXRESULTS, "20"));
				locatorJson.put(RLConstants.RADIUS, radiusList);
				locatorJson.put(RLConstants.SEARCHBOXDEFAULTTEXT,
						dialogProperties.get(RLConstants.SEARCHBOXDEFAULTTEXT, String.class));
				locatorJson.put(RLConstants.SEARCHFIELDHEADING,
						dialogProperties.get(RLConstants.SEARCHFIELDHEADING, "Enter Your Location"));
				locatorJson
						.put(RLConstants.LOCATEME, dialogProperties.get(RLConstants.LOCATEME, "LOCATE ME"));
				locatorJson.put(RLConstants.SEARCHBOXSUBMIT,
						dialogProperties.get(RLConstants.SEARCHBOXSUBMIT, "SUBMIT"));
				locatorJson
						.put(RLConstants.SHOWMORE, dialogProperties.get(RLConstants.SHOWMORE, "SHOW MORE"));
				locatorJson
						.put(RLConstants.SHOWLESS, dialogProperties.get(RLConstants.SHOWLESS, "SHOW LESS"));
				locatorJson
				.put(RLConstants.SHOWCUSTOMERFRIENDLYTEXT, dialogProperties.get(RLConstants.SHOWCUSTOMERFRIENDLYTEXT, StringUtils.EMPTY));
				locatorJson.put(RLConstants.RESTLOCPATH,
						dialogProperties.get(RLConstants.RESTLOCPATH, StringUtils.EMPTY) + ".html");
				locatorJson.put(RLConstants.RESTAURANTHOURSCLOSED,
						dialogProperties.get(RLConstants.RESTAURANTHOURSCLOSED, StringUtils.EMPTY));
				locatorJson.put(RLConstants.DISTANCEUNIT,
						dialogProperties.get(RLConstants.DISTANCEUNIT, StringUtils.EMPTY));
				locatorJson.put(RLConstants.ARIALABELFORDISTANCE,
						dialogProperties.get(RLConstants.ARIALABELFORDISTANCE, "how far away"));
				locatorJson.put(RLConstants.DISTANCEUNITLOCAL,
						getDistanceUnit(dialogProperties.get(RLConstants.DISTANCEUNIT, StringUtils.EMPTY), dialogProperties));
				locatorJson.put(RLConstants.FILTERSTRING, getFilterString());
				locatorJson.put(RLConstants.RESTAURANTNUMBERTEXT, dialogProperties.get(RLConstants.RESTAURANTNUMBERTEXT, "Restaurant Number"));
				locatorJson.put(RLConstants.PREFEREDTEXT, dialogProperties.get(RLConstants.PREFEREDTEXT));
				locatorJson.put(RLConstants.DIRECTIONLINKNAME,
						dialogProperties.get(RLConstants.DIRECTIONLINKNAME, "Get Directions"));
				locatorJson.put(RLConstants.PHONEHEADING,
						dialogProperties.get(RLConstants.PHONEHEADING, "Phone"));
				locatorJson.put(RLConstants.ENABLEHOURSEFROMSUNDAY,
						dialogProperties.get(RLConstants.ENABLEHOURSEFROMSUNDAY, StringUtils.EMPTY));
				locatorJson.put(RLConstants.ENABLEDRIVEHOURS,
						dialogProperties.get(RLConstants.ENABLEDRIVEHOURS, StringUtils.EMPTY));
				locatorJson.put(RLConstants.ENABLEFESTIVEHOURS,
						dialogProperties.get(RLConstants.ENABLEFESTIVEHOURS, StringUtils.EMPTY));
				locatorJson.put(RLConstants.ENABLEFAMILYEVENTS,
						dialogProperties.get(RLConstants.ENABLEFAMILYEVENTS, StringUtils.EMPTY));				
				locatorJson.put(RLConstants.OPENTIMEHEADING,
						dialogProperties.get(RLConstants.OPENTIMEHEADING, "OPEN UNTIL MIDNIGHT"));
				locatorJson.put(RLConstants.DRIVEOPENTEXT,
						dialogProperties.get(RLConstants.DRIVEOPENTEXT, "Drive Thru Open"));
				/*locatorJson.put(RLConstants.SPECIALOPENTEXT,
						dialogProperties.get(RLConstants.SPECIALOPENTEXT, "Open Now"));*/
				locatorJson.put(RLConstants.RESTAURANTCLOSEDTEXT,
						dialogProperties.get(RLConstants.RESTAURANTCLOSEDTEXT, "Closed Now"));
				locatorJson.put(RLConstants.SHOWHOURS,
						dialogProperties.get(RLConstants.SHOWHOURS, "Show Hours"));
				locatorJson.put(RLConstants.HIDEHOURS,
						dialogProperties.get(RLConstants.HIDEHOURS, "Hide Hours"));
				locatorJson.put(RLConstants.RESTAURANTHOURS,
						dialogProperties.get(RLConstants.RESTAURANTHOURS, "Restaurant Hours"));
				locatorJson.put(RLConstants.RESTAURANTCLOSEDTEXT,
						dialogProperties.get(RLConstants.RESTAURANTCLOSEDTEXT, "Restaurant Closed "));
				locatorJson.put(RLConstants.SERVICETEXT,
						dialogProperties.get(RLConstants.SERVICETEXT, "Service"));
				locatorJson.put(RLConstants.DRIVETHHOURS,
						dialogProperties.get(RLConstants.DRIVETHHOURS, "Drive-Thru Hours"));
				locatorJson.put(RLConstants.FESTIVEHOURS,
						dialogProperties.get(RLConstants.FESTIVEHOURS, "Festive Hours"));
				locatorJson.put(RLConstants.FAMILYEVENTHOURS,
						dialogProperties.get(RLConstants.FAMILYEVENTHOURS, "Family Event Hours"));
				/*locatorJson.put(RLConstants.SPECIALHOURS,
						dialogProperties.get(RLConstants.SPECIALHOURS, "Special Hours"));*/
				locatorJson.put(RLConstants.MONDAYTEXT, dialogProperties.get(RLConstants.MONDAYTEXT, "MON"));
				locatorJson.put(RLConstants.TUESDAYTEXT, dialogProperties.get(RLConstants.TUESDAYTEXT, "TUE"));
				locatorJson.put(RLConstants.WEDNESDAYTEXT, dialogProperties.get(RLConstants.WEDNESDAYTEXT, "WED"));
				locatorJson.put(RLConstants.THURSDAYTEXT, dialogProperties.get(RLConstants.THURSDAYTEXT, "THU"));
				locatorJson.put(RLConstants.FRIDAYTEXT, dialogProperties.get(RLConstants.FRIDAYTEXT, "FRI"));
				locatorJson.put(RLConstants.SATURDAYTEXT, dialogProperties.get(RLConstants.SATURDAYTEXT, "SAT"));
				locatorJson.put(RLConstants.SUNDAYTEXT, dialogProperties.get(RLConstants.SUNDAYTEXT, "SUN"));
				locatorJson.put(RLConstants.MONDAYTEXTFORARIALABEL, dialogProperties.get(RLConstants.MONDAYTEXTFORARIALABEL, "monday"));
				locatorJson.put(RLConstants.TUESDAYTEXTFORARIALABEL, dialogProperties.get(RLConstants.TUESDAYTEXTFORARIALABEL, "tuesday"));
				locatorJson.put(RLConstants.WEDNESDAYTEXTFORARIALABEL, dialogProperties.get(RLConstants.WEDNESDAYTEXTFORARIALABEL, "wednesday"));
				locatorJson.put(RLConstants.THURSDAYTEXTFORARIALABEL, dialogProperties.get(RLConstants.THURSDAYTEXTFORARIALABEL, "thursday"));
				locatorJson.put(RLConstants.FRIDAYTEXTFORARIALABEL, dialogProperties.get(RLConstants.FRIDAYTEXTFORARIALABEL, "friday"));
				locatorJson.put(RLConstants.SATURDAYTEXTFORARIALABEL, dialogProperties.get(RLConstants.SATURDAYTEXTFORARIALABEL, "saturday"));
				locatorJson.put(RLConstants.SUNDAYTEXTFORARIALABEL, dialogProperties.get(RLConstants.SUNDAYTEXTFORARIALABEL, "sunday"));
				locatorJson.put(RLConstants.CHANGELOCATION,
				dialogProperties.get(RLConstants.CHANGELOCATION, "Change Location"));
				locatorJson.put(RLConstants.TODAYRESTAURANTHOURS, dialogProperties.get(RLConstants.TODAYRESTAURANTHOURS,StringUtils.EMPTY));
				locatorJson.put(RLConstants.ORTEXT, dialogProperties.get(RLConstants.ORTEXT,StringUtils.EMPTY));
				locatorJson.put(RLConstants.AWAY, dialogProperties.get(RLConstants.AWAY, "Away"));
				locatorJson.put(RLConstants.DISTANCEUNIT,
						dialogProperties.get(RLConstants.DISTANCEUNIT, "Kilometer"));
				try {
					LOGGER.info("verify value");
					LOGGER.info("verify value" + dialogProperties.get(RLConstants.DRIVEHOURSCLOSED));
					locatorJson.put(RLConstants.DRIVEHOURSCLOSED, dialogProperties.get(RLConstants.DRIVEHOURSCLOSED));
				} catch (Exception e) {
					LOGGER.error("error while converting drive" + e.getMessage());
				}

				if (mcdWebServicesConfig != null) {
				int cookieExpiry = Integer.parseInt(mcdWebServicesConfig.getCookieExpiry());
				locatorJson.put(RLConstants.COOKIEEXPIRY,cookieExpiry);
				}
				locatorJson.put(RLConstants.RADIUS_NEAREST_LOCATION,
						dialogProperties.get(RLConstants.RADIUS_NEAREST_LOCATION, "5000"));
				locatorJson.put(RLConstants.ARIALABELSEARCHRESULT,
						dialogProperties.get(RLConstants.ARIALABELSEARCHRESULT, "search result list"));
				locatorJson.put(RLConstants.ARIALABELEXPANDRESTDETAIL,
						dialogProperties.get(RLConstants.ARIALABELEXPANDRESTDETAIL, "Expand restaurant details"));
				locatorJson.put(RLConstants.ARIALABELCLOSERESTDETAIL,
						dialogProperties.get(RLConstants.ARIALABELCLOSERESTDETAIL, "Close restaurant details"));
				
				locatorJson.put(RLConstants.ARIALABELCLOSERESTDETAIL,
						dialogProperties.get(RLConstants.ARIALABELCLOSERESTDETAIL, "Close restaurant details"));
				
				locatorJson.put(RLConstants.ARIALABELCLOSERESTDETAIL,
						dialogProperties.get(RLConstants.ARIALABELCLOSERESTDETAIL, "Close restaurant details"));
				
				locatorJson.put(RLConstants.COUNTRY_SPECIFIC_LATITUDE,
						dialogProperties.get(RLConstants.COUNTRY_SPECIFIC_LATITUDE, "41.8781"));
				
				locatorJson.put(RLConstants.COUNTRY_SPECIFIC_LONGITUDE,
						dialogProperties.get(RLConstants.COUNTRY_SPECIFIC_LONGITUDE, "-87.6298"));
				
				locatorJson.put(RLConstants.EMPTYSEARCHERRORMSG,
						dialogProperties.get(RLConstants.EMPTYSEARCHERRORMSG, "Please enter a zip code or city & state to search"));

				jsonData = locatorJson.toString();
			}
		} catch (JSONException e) {
			LOGGER.info("exception during the creation of RL JSON DATA" + e);
		}
		return jsonData;
	}

	/**
	 * Generates the JSON string containing all the ADA related texts.
	 * @return
	 */
	public String adaGenericTexts() {
		JSONObject adaJson = new JSONObject();
		String adaJsonData = StringUtils.EMPTY;
		try {

			//ValueMap dialogProperties = getSiteConfig(ADA_TEXT_SITE_CONFIG);
			ValueMap dialogProperties = null != adagenerictextsRes ? adagenerictextsRes.getValueMap() : null;
			if (dialogProperties != null) {

				adaJson.put(ADAConstants.PREVTEXT,
						dialogProperties.get(ADAConstants.PREVTEXT, "previous"));
				adaJson.put(ADAConstants.NEXTTEXT,
						dialogProperties.get(ADAConstants.NEXTTEXT, "next"));
				adaJson.put(ADAConstants.PLAYTEXT,
						dialogProperties.get(ADAConstants.PLAYTEXT, "play"));
				adaJson.put(ADAConstants.PAUSETEXT,
						dialogProperties.get(ADAConstants.PAUSETEXT, "pause"));
				adaJson.put(ADAConstants.CLICKTOPLAYTEXT,
						dialogProperties.get(ADAConstants.CLICKTOPLAYTEXT, "click to play"));				
				adaJson.put(ADAConstants.CLICKTOPAUSETEXT,
						dialogProperties.get(ADAConstants.CLICKTOPAUSETEXT, "click to pause"));
				adaJson.put(ADAConstants.SLIDERPLAYINGTEXT,
						dialogProperties.get(ADAConstants.SLIDERPLAYINGTEXT, "slider playing"));
				adaJson.put(ADAConstants.SLIDERPAUSEDTEXT,
						dialogProperties.get(ADAConstants.SLIDERPAUSEDTEXT, "slider paused"));
				adaJson.put(ADAConstants.ACTIVESLIDETEXT,
						dialogProperties.get(ADAConstants.ACTIVESLIDETEXT, "active slide"));
				adaJson.put(ADAConstants.GOTOSLIDETEXT,
						dialogProperties.get(ADAConstants.GOTOSLIDETEXT, "go to slide"));
				adaJson.put(ADAConstants.VISITPREVSLIDETEXT,
						dialogProperties.get(ADAConstants.VISITPREVSLIDETEXT, "visit previous slide"));
				adaJson.put(ADAConstants.VISITNEXTSLIDETEXT,
						dialogProperties.get(ADAConstants.VISITNEXTSLIDETEXT, "visit next slide"));
				adaJson.put(ADAConstants.EXPANDEDTEXT,
						dialogProperties.get(ADAConstants.EXPANDEDTEXT, "expanded"));
				adaJson.put(ADAConstants.COLLAPSEDTEXT,
						dialogProperties.get(ADAConstants.COLLAPSEDTEXT, "collapsed"));
				adaJson.put(ADAConstants.SELECTEDTEXT,
						dialogProperties.get(ADAConstants.SELECTEDTEXT, "selected"));
				adaJson.put(ADAConstants.CLICKTOPLAYVIDEOTEXT,
						dialogProperties.get(ADAConstants.CLICKTOPLAYVIDEOTEXT, "click to play this video"));
				adaJson.put(ADAConstants.MONTH_LIST,
						Arrays.toString(dialogProperties.get(ADAConstants.MONTH_LIST, monthOptionValue)));
				adaJsonData = adaJson.toString();
			}
		} catch (JSONException e) {
			LOGGER.info("exception during the creation of RL JSON DATA" + e);
		}
		return adaJsonData;
	}
	
	private void populateHeaderLinks(Resource resource) {
		List<Map<String, String>> props = MultiFieldPanelUtil.getMultiFieldPanelValues(resource, "links");

		if (props != null && !props.isEmpty()) {
			LOGGER.debug("{} header links available", props.size());
			this.headerLinks = new ArrayList<Link>();
			//BumperUtil bumperUtil = new BumperUtil(getSiteConfigResource(ApplicationConstants.RES_BUMPER));
			BumperUtil bumperUtil = new BumperUtil(bumperRes);
			Iterator<Map<String,String>> itr = props.iterator();

			while(itr.hasNext()) {
				Map<String, String> prop = itr.next();
				LOGGER.debug("Properties are {}", prop);
				Link channel = bumperUtil.getLink(prop.get(ApplicationConstants.PN_PATH),
						prop.get(ApplicationConstants.PN_TITLE),
						Boolean.valueOf(prop.get(ApplicationConstants.PN_TARGET)));
				this.headerLinks.add(channel);
			}
		}
	}

	public String getRadiusSelected() {
		return radiusSelected;
	}

	public String getSubmit() {
		return submit;
	}

	public String getLocate() {
		return locate;
	}

	public List<String> getFilterSelected() {
		return filterSelected;
	}

	public String getRestaurantSearchText() {
		return restaurantSearchText;
	}

	public boolean isLanguageToggleEnabled() {
		return this.languageToggleEnabled;
	}

	public boolean isMyAccountEnabled() {
		return this.myAccountEnabled;
	}

	public String getFilterString() {
		return this.filterString;
	}

	public boolean isRlPage() {
		return isRlPage;
	}

	public List<Link> getHeaderLinks() {
		return headerLinks;
	}

	public Link getJoinLink() { return joinLink; }

	public String getPageNameForAnalytics() {
		return pageNameForAnalytics;
	}

	public boolean isJoinLinkEnabled() {
		return this.joinLinkEnabled;
	}

	public String getMyAccountText() {
		return myAccountText;
	}

	public boolean isLocateLinkEnabled() {
		return this.locateLinkEnabled;
	}
	
	public String getTopNavigationAria() {
		return topNavigationAria;
	}
}
