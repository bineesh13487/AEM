package com.mcd.rwd.us.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.mcd.rwd.global.core.utils.ConnectionUtil;
import com.mcd.rwd.us.core.bean.rl.RestLocDayOfWeekService;
import com.mcd.rwd.us.core.bean.rl.RestLocFeatures;
import com.mcd.rwd.us.core.bean.rl.RestLocGeometry;
import com.mcd.rwd.us.core.bean.rl.RestLocPhoneNumber;
import com.mcd.rwd.us.core.bean.rl.RestLocProperties;
import com.mcd.rwd.us.core.bean.rl.RestLocStoreAttribute;
import com.mcd.rwd.us.core.bean.rl.RestLocatorOldResponse;
import com.mcd.rwd.us.core.bean.rl.RestLocatorUrl;
import com.mcd.rwd.us.core.bean.rl.RestaurantLocatorResponse;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;

/**
 * Created by Srishma Yarra
 */

@SuppressWarnings("serial") @SlingServlet(paths = "/services/mcd/us/restaurantLocator") @Properties({
		@Property(name = "service.pid", value = "com.mcd.rwd.us.core.servlets.RestaurantLocator"),
		@Property(name = "service.description", value = "provides the list of restaurants"),
		@Property(name = "service.vendor", value = "HCL Technologies") }) 
public class RestaurantsUsrLocator
		extends SlingSafeMethodsServlet {
	private static final String HTMLCONSTANTS = "%02d";
	private StringBuffer DESIGNPATH=new StringBuffer("/etc/designs/mcd");
	transient Node node = null;

	/**
	 * default logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantsUsrLocator.class);

	@Reference private transient McdFactoryConfigConsumer mcdFactoryConfigConsumer;

	@Reference private transient McdWebServicesConfig mcdWebServicesConfig;

	@Override protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		DESIGNPATH=new StringBuffer("/etc/designs/mcd");

		String searchLocationLatitude = request.getParameter("latitude");
		String searchLocationLongitude = request.getParameter("longitude");
		String restaurantRadius = request.getParameter("radius");
		String maxResults = request.getParameter("maxResults");
		String pageCountry = request.getParameter("country");
		String pageLanguage = request.getParameter("language");
		if(pageCountry!=null) {
		DESIGNPATH=DESIGNPATH.append("/"+pageCountry);
		}
		if(pageLanguage!=null) {
			DESIGNPATH=DESIGNPATH.append("/"+pageLanguage);
		}
		if(null!=request.getResourceResolver()) {
Resource resource = request.getResourceResolver().getResource(DESIGNPATH.toString()+"/jcr:content/rl");
if(null != resource) {
 node = resource.adaptTo(Node.class);
}
		}
		
if (pageCountry != null && pageLanguage != null) {
			McdFactoryConfig mcdFactoryConfig = mcdFactoryConfigConsumer
					.getMcdFactoryConfig(pageCountry, pageLanguage);
			if (mcdFactoryConfig != null) {
				String restaurantLocatorUrl = mcdWebServicesConfig.getRestaurantLocatorUrl();
				String restaurantLocatorApiKey = mcdFactoryConfig.getRestaurantLocatorApiKey();
				String restaurantLocatorCountry = mcdFactoryConfig.getRestaurantLocatorCountry();
				String restaurantLocatorLanguage = mcdFactoryConfig.getRestaurantLocatorLanguage();
				
				if (searchLocationLatitude != null && searchLocationLongitude != null) {
					String requiredRestaurantLocatorUrl =
							restaurantLocatorUrl + "?filter=geodistance&coords=" + searchLocationLatitude
									+ "," + searchLocationLongitude + "&distance=" + restaurantRadius
									+ "&market=" + restaurantLocatorCountry + "&languageName="
									+ restaurantLocatorLanguage + "&size=" + maxResults;
					response.setContentType(ApplicationConstants.CONTENT_TYPE_JSON);
					PrintWriter out = response.getWriter();
					out.print(fetchRestaurantFromNewService(requiredRestaurantLocatorUrl,
							restaurantLocatorApiKey));
				} else {
					LOGGER.error("No Latitude and longitude provided");
				}
			} else {
				LOGGER.error("No configuration found for the country - {} and language- {}", pageCountry,
						pageLanguage);
			}
		} else {
			LOGGER.error("No country or language found");
		}
	}


	public String fetchRestaurantFromNewService(String newRlUrl, String rlApiKey) {
		Map<String, String> header = new HashMap<String, String>();
		header.put("mcd_apikey", rlApiKey);
		ConnectionUtil conUtil = new ConnectionUtil();
		String rlResponse = conUtil.sendGet(newRlUrl, header);
		LOGGER.info("locator api response for url " + newRlUrl + "\n" + rlResponse);
		RestaurantLocatorResponse[] rlObjectResponse = new Gson()
				.fromJson(rlResponse, RestaurantLocatorResponse[].class);
		return convertResponse(rlObjectResponse);
	}
	public RestLocProperties setStoreService(RestLocProperties restProperties,RestaurantLocatorResponse newRest) {
		if (newRest.getStoreServices() != null) {
			List<RestLocDayOfWeekService> dayOfWeekServices = newRest.getStoreServices()
					.getDayofweekservice();
			List<RestLocDayOfWeekService> specialDayService = newRest.getStoreServices()
					.getSpecialdayservice();
			setResturantStartEndTime(restProperties, dayOfWeekServices);
			setResturanTodayStartEndTime(restProperties, dayOfWeekServices);
			setResturanTodaySpecialStartEndTime(restProperties, specialDayService);
			setResturantSpecialStartEndTime(restProperties, specialDayService);

		}
return restProperties;
	}
	
	public RestLocProperties setRestLocPhoneNo(RestLocProperties restProperties ,RestaurantLocatorResponse newRest) {
		if (newRest.getStoreNumbers() != null) {
			List<RestLocPhoneNumber> phoneNumbers = newRest.getStoreNumbers().getPhonenumber();
			if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
				RestLocPhoneNumber phoneNumber = phoneNumbers.get(0);
				restProperties.setTelephone(phoneNumber.getNumber());
			}
		}
		return restProperties;
	}
	
	public RestLocProperties setRestLocProperties(RestaurantLocatorResponse newRest) {
		RestLocProperties restProperties = new RestLocProperties();
		if (newRest.getAddress() != null) {
			
			restProperties.setaddressLine1(newRest.getAddress().getAddressLine1());
			restProperties.setaddressLine2(newRest.getAddress().getRegion());
			restProperties.setaddressLine3(newRest.getAddress().getCityTown());
			restProperties.setaddressLine4(newRest.getAddress().getCountry());
			restProperties.setSubDivision(newRest.getAddress().getSubdivision());
			restProperties.setPostcode(newRest.getAddress().getPostalZip());
			restProperties.setShortDescription(newRest.getShortDescription());
			restProperties.setLongDescription(newRest.getLongDescription());
			restProperties.setIdentifier(newRest.getIdentifier());
			restProperties.setName(newRest.getPublicName());
			restProperties.setId(newRest.getId());
					if (newRest.getStoreAttributes() != null) {
				setRestaurantFacilities(newRest, restProperties);
			}
					
					restProperties=setStoreService(restProperties, newRest);
					restProperties=setRestLocPhoneNo(restProperties, newRest);
			
			if (newRest.getUrls() != null && newRest.getUrls().getUrl() != null) {
				List<RestLocatorUrl> restUrlList = newRest.getUrls().getUrl();
				for (RestLocatorUrl restUrl : restUrlList) {
					if (restUrl.getUrlType() != null && "JOBAPPLICATION".equalsIgnoreCase(restUrl.getUrlType())) {
						restProperties.setRestaurantUrl(restUrl.getUrl());
					} else if(restUrl.getUrlType() != null && "STORE".equalsIgnoreCase(restUrl.getUrlType())) {
						restProperties.setJobUrl(restUrl.getUrl());
						
					}
				}
			}



			
		}
		return restProperties;
	}

	public String convertResponse(RestaurantLocatorResponse[] newResponse) {

		RestLocatorOldResponse oldResponse = new RestLocatorOldResponse();
		List<RestLocFeatures> restLocFeatures = new LinkedList<RestLocFeatures>();
		for (int i = 0; i < newResponse.length; i++) {
			RestaurantLocatorResponse newRest = newResponse[i];
			if (newRest != null) {
				RestLocFeatures feature = new RestLocFeatures();
				RestLocGeometry restGeometry = new RestLocGeometry();
			
				
				if (newRest.getAddress().getLocation() != null) {
					double[] coordinates = new double[] { newRest.getAddress().getLocation().getLon(),
							newRest.getAddress().getLocation().getLat() };
					restGeometry.setCoordinates(coordinates);
				}
				feature.setGeometry(restGeometry);
				feature.setProperties(setRestLocProperties(newRest));
				restLocFeatures.add(feature);
			}
		}
		oldResponse.setFeatures(restLocFeatures);

		return new Gson().toJson(oldResponse);

	}
	
	public String convertMillis(RestLocDayOfWeekService weekService) {
		return convert24To12(
				convertMillisToTime(weekService.getStartTime(),
						weekService.getEndTime()));
	}

	public RestLocProperties setRestTime(String dayOfWeek,RestLocProperties restProperties ,RestLocDayOfWeekService weekService) {
		if ("MONDAY".equalsIgnoreCase(dayOfWeek)) {
			restProperties.sethoursMonday(convert24To12(
					convertMillisToTime(weekService.getStartTime(),
							weekService.getEndTime())));
		} else if ("TUESDAY".equalsIgnoreCase(dayOfWeek)) {
			restProperties.sethoursTuesday(convert24To12(
					convertMillisToTime(weekService.getStartTime(),
							weekService.getEndTime())));
		} else if ("WEDNESDAY".equalsIgnoreCase(dayOfWeek)) {
			restProperties.sethoursWednesday(convert24To12(
					convertMillisToTime(weekService.getStartTime(),
							weekService.getEndTime())));
		} else if ("THURSDAY".equalsIgnoreCase(dayOfWeek)) {
			restProperties.sethoursThursday(convert24To12(
					convertMillisToTime(weekService.getStartTime(),
							weekService.getEndTime())));
		} else if ("FRIDAY".equalsIgnoreCase(dayOfWeek)) {
			restProperties.sethoursFriday(convert24To12(
					convertMillisToTime(weekService.getStartTime(),
							weekService.getEndTime())));
		} else if ("SATURDAY".equalsIgnoreCase(dayOfWeek)) {
			restProperties.sethoursSaturday(convert24To12(
					convertMillisToTime(weekService.getStartTime(),
							weekService.getEndTime())));
		} else if ("SUNDAY".equalsIgnoreCase(dayOfWeek)) {
			restProperties.sethoursSunday(convert24To12(
					convertMillisToTime(weekService.getStartTime(),
							weekService.getEndTime())));
		}
	return restProperties;	
	}
	
	public RestLocProperties setRestDriverTime(String dayOfWeek,RestLocProperties restProperties ,RestLocDayOfWeekService weekService) {
		if ("MONDAY".equalsIgnoreCase(dayOfWeek)) {
			restProperties.setdriveHoursMonday(convert24To12(
					convertMillisToTime(weekService.getStartTime(),
							weekService.getEndTime())));
		} else if ("TUESDAY".equalsIgnoreCase(dayOfWeek)) {
			restProperties.setdriveHoursTuesday(convert24To12(
					convertMillisToTime(weekService.getStartTime(),
							weekService.getEndTime())));
		} else if ("WEDNESDAY".equalsIgnoreCase(dayOfWeek)) {
			restProperties.setdriveHoursWednesday(convert24To12(
					convertMillisToTime(weekService.getStartTime(),
							weekService.getEndTime())));
		} else if ("THURSDAY".equalsIgnoreCase(dayOfWeek)) {
			restProperties.setdriveHoursThursday(convert24To12(
					convertMillisToTime(weekService.getStartTime(),
							weekService.getEndTime())));
		} else if ("FRIDAY".equalsIgnoreCase(dayOfWeek)) {
			restProperties.setdriveHoursFriday(convert24To12(
					convertMillisToTime(weekService.getStartTime(),
							weekService.getEndTime())));
		} else if ("SATURDAY".equalsIgnoreCase(dayOfWeek)) {
			restProperties.setdriveHoursSaturday(convert24To12(
					convertMillisToTime(weekService.getStartTime(),
							weekService.getEndTime())));
		} else if ("SUNDAY".equalsIgnoreCase(dayOfWeek)) {
			restProperties.setdriveHoursSunday(convert24To12(
					convertMillisToTime(weekService.getStartTime(),
							weekService.getEndTime())));
		}

		
		return restProperties;
	}


	/**
	 * @param restProperties
	 * @param dayOfWeekServices
	 */
	private void setResturantStartEndTime(RestLocProperties restProperties,
			List<RestLocDayOfWeekService> dayOfWeekServices) {
		if (dayOfWeekServices != null && !dayOfWeekServices.isEmpty()) {
			for (RestLocDayOfWeekService weekService : dayOfWeekServices) {
				String dayOfWeek = weekService.getDayOfWeek();
				if (dayOfWeek != null) {
					if (null==weekService.getService() || "LOBBY".equalsIgnoreCase(weekService.getService())) {

						restProperties=setRestTime(dayOfWeek, restProperties, weekService);
					} else {
						restProperties=setRestDriverTime(dayOfWeek, restProperties, weekService);
					}
				}
			}

		}
	}

	public String convertHours(String hour24Time) {
		try {

			SimpleDateFormat hour24Sdf = new SimpleDateFormat("H:mm");
			SimpleDateFormat hour12Sdf = new SimpleDateFormat("h:mm a");
			Date hour24Dt = hour24Sdf.parse(hour24Time);

			return hour12Sdf.format(hour24Dt);
		} catch (Exception e) {
			LOGGER.error("Exception converting hours", e);
		}
		return hour24Time;

	}

	public String convert24To12(String hours) {
		String[] splitHours = hours.split("-");
		if (splitHours.length > 1) {
			StringBuilder hoursConverted = new StringBuilder();
			hoursConverted.append(convertHours(splitHours[0].trim())).append("-")
					.append(convertHours(splitHours[1].trim()));
			return hoursConverted.toString();
		}
		return hours;
	}

	/**
	 * @param restProperties
	 * @param dayOfWeekServices
	 */

	public void setResturanTodaySpecialStartEndTime(RestLocProperties restProperties,
			List<RestLocDayOfWeekService> dayOfWeekServices) {
		if (dayOfWeekServices != null && !dayOfWeekServices.isEmpty()) {

			Map<Integer, String> weekMap = new TreeMap<Integer, String>();
			weekMap.put(1, "SUNDAY");
			weekMap.put(2, "MONDAY");
			weekMap.put(3, "TUESDAY");
			weekMap.put(4, "WEDNESDAY");
			weekMap.put(5, "THURSDAY");
			weekMap.put(6, "FRIDAY");
			weekMap.put(7, "SATURDAY");

			for (RestLocDayOfWeekService weekService : dayOfWeekServices) {
				int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

				String dayOfWeek = weekService.getDayOfWeek();
				if (weekService.getService() == null || "LOBBY".equalsIgnoreCase(weekService.getService())) {
					if (null != dayOfWeek && dayOfWeek.equalsIgnoreCase(weekMap.get(day))) {
						restProperties.setTodaySpecialHours(convert24To12(
								convertMillisToTime(weekService.getStartTime(), weekService.getEndTime())));
					}
				} else {
					if (dayOfWeek != null && dayOfWeek.equalsIgnoreCase(weekMap.get(day))) {
						restProperties.setDriveTodaySpecialHours(convert24To12(
								convertMillisToTime(weekService.getStartTime(), weekService.getEndTime())));
					}
				}
			}
		}
	}

	/**
	 * @param restProperties
	 * @param dayOfWeekServices
	 */

	public void setResturantSpecialStartEndTime(RestLocProperties restProperties,
			List<RestLocDayOfWeekService> dayOfWeekServices) {
		if (dayOfWeekServices != null && !dayOfWeekServices.isEmpty()) {

			Map<Integer, String> weekMap = new TreeMap<Integer, String>();
			weekMap.put(1, "SUNDAY");
			weekMap.put(2, "MONDAY");
			weekMap.put(3, "TUESDAY");
			weekMap.put(4, "WEDNESDAY");
			weekMap.put(5, "THURSDAY");
			weekMap.put(6, "FRIDAY");
			weekMap.put(7, "SATURDAY");

			for (RestLocDayOfWeekService weekService : dayOfWeekServices) {
				int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

				String dayOfWeek = weekService.getDayOfWeek();
				if (weekService.getService() == null || "LOBBY".equalsIgnoreCase(weekService.getService())) {
					if (dayOfWeek != null && dayOfWeek.equalsIgnoreCase(weekMap.get(day))) {
						restProperties.setSpecialHours(convert24To12(
								convertMillisToTime(weekService.getStartTime(), weekService.getEndTime())));
					}
				} else {
					if (dayOfWeek != null && dayOfWeek.equalsIgnoreCase(weekMap.get(day))) {
						restProperties.setDriveSpecialHours(convert24To12(
								convertMillisToTime(weekService.getStartTime(), weekService.getEndTime())));
					}
				}
			}
		}
	}

	/**
	 * @param restProperties
	 * @param dayOfWeekServices
	 */
	public void setResturanTodayStartEndTime(RestLocProperties restProperties,
			List<RestLocDayOfWeekService> dayOfWeekServices) {
		if (dayOfWeekServices != null && !dayOfWeekServices.isEmpty()) {

			Map<Integer, String> weekMap = new TreeMap<Integer, String>();
			weekMap.put(1, "SUNDAY");
			weekMap.put(2, "MONDAY");
			weekMap.put(3, "TUESDAY");
			weekMap.put(4, "WEDNESDAY");
			weekMap.put(5, "THURSDAY");
			weekMap.put(6, "FRIDAY");
			weekMap.put(7, "SATURDAY");

			for (RestLocDayOfWeekService weekService : dayOfWeekServices) {
				int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

				String dayOfWeek = weekService.getDayOfWeek();
				if (weekService.getService() == null || "LOBBY".equals(weekService.getService())) {
					if (dayOfWeek != null && dayOfWeek.equalsIgnoreCase(weekMap.get(day))) {
						restProperties.setTodayHours(convert24To12(
								convertMillisToTime(weekService.getStartTime(), weekService.getEndTime())));
					}
				} else {
					if (dayOfWeek != null && dayOfWeek.equalsIgnoreCase(weekMap.get(day))) {
						restProperties.setDriveTodayHours(convert24To12(
								convertMillisToTime(weekService.getStartTime(), weekService.getEndTime())));
					}
				}
			}
		}
	}

	/**
	 * @param newRest
	 * @param restProperties
	 */
	public void setRestaurantFacilities(RestaurantLocatorResponse newRest,
			RestLocProperties restProperties) {
		List<RestLocStoreAttribute> storeAttributes = newRest.getStoreAttributes().getAttribute();
		for (RestLocStoreAttribute attribute : storeAttributes) {
			String attributeType = attribute.getType();
			restProperties.getFilterType().add(attributeType);
			
		}
	}

	public  String convertMillisToTime(long startTimeInMillis, long endTimeInMillis) {
		Calendar calender = Calendar.getInstance();
		calender.setTimeZone(TimeZone.getTimeZone("EST"));
		calender.setTimeInMillis(startTimeInMillis);
		String startHourOfDay = String.format(HTMLCONSTANTS, calender.get(Calendar.HOUR_OF_DAY));
		String startMinuteOfHour = String.format(HTMLCONSTANTS, calender.get(Calendar.MINUTE));
		calender.setTimeInMillis(endTimeInMillis);
		String endHourOfDay = String.format(HTMLCONSTANTS, calender.get(Calendar.HOUR_OF_DAY));
		String endMinuteOfHour = String.format(HTMLCONSTANTS, calender.get(Calendar.MINUTE));

		String restaurantStartHours = startHourOfDay + ":" + startMinuteOfHour;
		String restaurantEndHours = endHourOfDay + ":" + endMinuteOfHour;
		if (restaurantStartHours.equals(restaurantEndHours)) {
			try {
				if(null!=node && node.hasProperty("Hours24Text")) {
					return node.getProperty("Hours24Text").getString();
				}else {
					return "Open 24Hr";		
				}
			} catch (RepositoryException e) {
				LOGGER.info("Exception while reading 24hours text",e);
			}
			
		} 
			return restaurantStartHours + " - " + restaurantEndHours;
	
			
		
	}
}
