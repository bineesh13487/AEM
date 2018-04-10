package com.mcd.rwd.global.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.inject.Inject;

/**
 * Created by Rakesh.Balaiah on 13-04-2016.
 */
@Model(adaptables = Resource.class) public class SmartBanner {

	@Inject @Optional @Default(booleanValues = false) private boolean enabled;

	@Inject @Optional private String iosId;

	@Inject @Optional private String androidId;

	@Inject @Optional private String windowsId;

	@Inject @Optional private String title;

	@Inject @Optional private String desc;

	@Inject @Optional private String icon;

	@Inject @Optional private String url;
	
	@Inject @Optional @Default(values = "Close Banner") private String aria;

	@Inject @Optional @Default(values = "Download App Now") private String buttonText;

	@Inject @Optional @Default(intValues = 15) private int daysHidden;

	@Inject @Optional @Default(intValues = 90) private int daysReminder;

	public boolean isEnabled() {
		return enabled;
	}

	public String getIosId() {
		return iosId;
	}

	public String getAndroidId() {
		return androidId;
	}

	public String getWindowsId() {
		return windowsId;
	}

	public String getTitle() {
		return title;
	}

	public String getDesc() {
		return desc;
	}

	public String getIcon() {
		return icon;
	}

	public String getUrl() {
		return url;
	}

	public String getButtonText() {
		return buttonText;
	}

	public int getDaysHidden() {
		return daysHidden;
	}

	public int getDaysReminder() {
		return daysReminder;
	}

	public String getAria() {
		return aria;
	}

}
