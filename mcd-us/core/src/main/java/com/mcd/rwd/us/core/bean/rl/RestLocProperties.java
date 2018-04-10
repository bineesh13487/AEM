package com.mcd.rwd.us.core.bean.rl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Srishma Yarra.
 */
public class RestLocProperties {

	private String name;

	private String shortDescription;
	
	private String jobUrl;
	
	private String longDescription;
	
	private String todaySpecialHours;
	
	private String driveTodaySpecialHours;
	
	private String todayHours;
	
	private String driveTodayHours;

	private String id;
	
	private List<String> filterType=new ArrayList<String>();
	

	private String addressLine1;

	private String addressLine2;

	private String addressLine3;

	private String addressLine4;
	
	private String subDivision;

	private String postcode;

	private String telephone;

	private String hoursMonday;

	private String hoursTuesday;

	private String hoursWednesday;

	private String hoursThursday;

	private String hoursFriday;

	private String hoursSaturday;

	private String hoursSunday;
	
	private Identifiers identifiers;

	private String driveHoursMonday;

	private String driveHoursTuesday;

	private String driveHoursWednesday;

	private String driveHoursThursday;

	private String driveHoursFriday;

	private String driveHoursSaturday;

	private String driveHoursSunday;

	private String birthDaysParties = "0";

	private String driveThru = "0";

	private String outDoorPlayGround = "0";

	private String indoorPlayGround = "0";

	private String wifi = "0";

	private String breakFast = "0";

	private String nightMenu = "0";

	private String giftCards = "0";

	private String mobileOffers = "0";

	private String restaurantUrl;

	private String specialHours;
	
	private String specialDriveHours;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getaddressLine1() {
		return addressLine1;
	}

	public void setaddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getaddressLine2() {
		return addressLine2;
	}

	public void setaddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getaddressLine3() {
		return addressLine3;
	}

	public void setaddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getaddressLine4() {
		return addressLine4;
	}

	public void setaddressLine4(String addressLine4) {
		this.addressLine4 = addressLine4;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String gethoursMonday() {
		return hoursMonday;
	}

	public void sethoursMonday(String hoursMonday) {

		this.hoursMonday = hoursMonday;
	}

	public String gethoursTuesday() {
		return hoursTuesday;
	}

	public void sethoursTuesday(String hoursTuesday) {
		this.hoursTuesday = hoursTuesday;
	}

	public String gethoursWednesday() {
		return hoursWednesday;
	}

	public void sethoursWednesday(String hoursWednesday) {
		this.hoursWednesday = hoursWednesday;
	}

	public String gethoursThursday() {
		return hoursThursday;
	}

	public void sethoursThursday(String hoursThursday) {
		this.hoursThursday = hoursThursday;
	}

	public String gethoursFriday() {
		return hoursFriday;
	}

	public void sethoursFriday(String hoursFriday) {
		this.hoursFriday = hoursFriday;
	}

	public String gethoursSaturday() {
		return hoursSaturday;
	}

	public void sethoursSaturday(String hoursSaturday) {
		this.hoursSaturday = hoursSaturday;
	}

	public String gethoursSunday() {
		return hoursSunday;
	}

	public void sethoursSunday(String hoursSunday) {
		this.hoursSunday = hoursSunday;
	}

	public String getbirthDaysParties() {
		return birthDaysParties;
	}

	public void setbirthDaysParties(String birthDaysParties) {
		this.birthDaysParties = birthDaysParties;
	}

	public String getdriveThru() {
		return driveThru;
	}

	public void setdriveThru(String driveThru) {
		this.driveThru = driveThru;
	}

	public String getoutDoorPlayGround() {
		return outDoorPlayGround;
	}

	public void setoutDoorPlayGround(String outDoorPlayGround) {
		this.outDoorPlayGround = outDoorPlayGround;
	}

	public String getindoorPlayGround() {
		return indoorPlayGround;
	}

	public void setindoorPlayGround(String indoorPlayGround) {
		this.indoorPlayGround = indoorPlayGround;
	}

	public String getwifi() {
		return wifi;
	}

	public void setwifi(String wifi) {
		this.wifi = wifi;
	}

	public String getbreakFast() {
		return breakFast;
	}

	public void setbreakFast(String breakFast) {
		this.breakFast = breakFast;
	}

	public String getnightMenu() {
		return nightMenu;
	}

	public void setnightMenu(String nightMenu) {
		this.nightMenu = nightMenu;
	}

	public String getgiftCards() {
		return giftCards;
	}

	public void setgiftCards(String giftCards) {
		this.giftCards = giftCards;
	}

	public String getmobileOffers() {
		return mobileOffers;
	}

	public void setmobileOffers(String mobileOffers) {
		this.mobileOffers = mobileOffers;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getRestaurantUrl() {
		return restaurantUrl;
	}

	public void setRestaurantUrl(String restaurantUrl) {
		this.restaurantUrl = restaurantUrl;
	}

	public String getSpecialHours() {
		return specialHours;
	}

	public void setSpecialHours(String specialHours) {
		this.specialHours = specialHours;
	}
	
	public void setDriveSpecialHours(String specialHours) {
		this.specialDriveHours=specialHours;
	}

	public String getdriveHoursMonday() {
		return driveHoursMonday;
	}

	public void setdriveHoursMonday(String driveHoursMonday) {
		this.driveHoursMonday = driveHoursMonday;
	}

	public String getdriveHoursTuesday() {
		return driveHoursTuesday;
	}

	public void setdriveHoursTuesday(String driveHoursTuesday) {
		this.driveHoursTuesday = driveHoursTuesday;
	}

	public String getdriveHoursWednesday() {
		return driveHoursWednesday;
	}

	public void setdriveHoursWednesday(String driveHoursWednesday) {
		this.driveHoursWednesday = driveHoursWednesday;
	}

	public String getdriveHoursThursday() {
		return driveHoursThursday;
	}

	public void setdriveHoursThursday(String driveHoursThursday) {
		this.driveHoursThursday = driveHoursThursday;
	}

	public String getdriveHoursFriday() {
		return driveHoursFriday;
	}

	public void setdriveHoursFriday(String driveHoursFriday) {
		this.driveHoursFriday = driveHoursFriday;
	}

	public String getdriveHoursSaturday() {
		return driveHoursSaturday;
	}

	public void setdriveHoursSaturday(String driveHoursSaturday) {
		this.driveHoursSaturday = driveHoursSaturday;
	}

	public String getdriveHoursSunday() {
		return driveHoursSunday;
	}

	public void setdriveHoursSunday(String driveHoursSunday) {
		this.driveHoursSunday = driveHoursSunday;
	}

	public String getSpecialDriveHours() {
		return specialDriveHours;
	}

	public void setSpecialDriveHours(String specialDriveHours) {
		this.specialDriveHours = specialDriveHours;
	}

	public String getTodayHours() {
		return todayHours;
	}

	public void setTodayHours(String todayHours) {
		this.todayHours = todayHours;
	}

	public String getDriveTodayHours() {
		return driveTodayHours;
	}

	public void setDriveTodayHours(String todayDriveHours) {
		this.driveTodayHours = todayDriveHours;
	}

	public String getTodaySpecialHours() {
		return todaySpecialHours;
	}

	public void setTodaySpecialHours(String todaySpecialHours) {
		this.todaySpecialHours = todaySpecialHours;
	}

	public String getDriveTodaySpecialHours() {
		return driveTodaySpecialHours;
	}

	public void setDriveTodaySpecialHours(String driveTodaySpecialHours) {
		this.driveTodaySpecialHours = driveTodaySpecialHours;
	}

	public String getSubDivision() {
		return subDivision;
	}

	public void setSubDivision(String subDivision) {
		this.subDivision = subDivision;
	}

	public Identifiers getIdentifier() {
		return identifiers;
	}

	public void setIdentifier(Identifiers identifiers) {
		StoreIdentifier[] storeIdentifier = identifiers.getStoreIdentifier();
		for(int i=0;i<storeIdentifier.length;i++) {
			storeIdentifier[i].setIdentifierType(storeIdentifier[i].getIdentifierType().replaceAll(",", " "));
			storeIdentifier[i].setIdentifierValue(storeIdentifier[i].getIdentifierValue().replaceAll(",", " "));
		}
		this.identifiers = identifiers;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public List<String> getFilterType() {
		return filterType;
	}

	public void setFilterType(List<String> filterType) {
		this.filterType = filterType;
	}

	public String getJobUrl() {
		return jobUrl;
	}

	public void setJobUrl(String jobUrl) {
		this.jobUrl = jobUrl;
	}

}
