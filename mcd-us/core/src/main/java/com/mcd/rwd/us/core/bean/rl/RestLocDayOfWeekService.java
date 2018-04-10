package com.mcd.rwd.us.core.bean.rl;
/**
 * Created by srishma yarra 
 */
public class RestLocDayOfWeekService {

	private String service;

	private long startTime;

	private long endTime;

	private String dayOfWeek;

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
}
