package com.mcd.rwd.us.core.bean.rl;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Srishma Yarra
 */
public class RestLocStoreServices {

	private List<RestLocDayOfWeekService> dayofweekservice;
	private List<RestLocDayOfWeekService> specialdayservice;

	public List<RestLocDayOfWeekService> getDayofweekservice() {
		return dayofweekservice;
	}

	public void setDayofweekservice(
			List<RestLocDayOfWeekService> dayofweekservice) {
		this.dayofweekservice = dayofweekservice;
	}

	public List<RestLocDayOfWeekService> getSpecialdayservice() {
		return specialdayservice;
	}

	public void setSpecialdayservice(List<RestLocDayOfWeekService> specialdayservice) {
		this.specialdayservice = specialdayservice;
	}

}
