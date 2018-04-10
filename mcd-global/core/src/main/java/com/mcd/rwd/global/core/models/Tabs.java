package com.mcd.rwd.global.core.models;

import com.mcd.rwd.global.core.constants.ApplicationConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by Rakesh.Balaiah on 10-04-2016.
 */
@Model(adaptables = Resource.class)
public class Tabs {

	@Inject @Optional @Named(value = "tabs")
	private List<String> tabsList;

	@Inject @Optional @Default(booleanValues = false)
	private boolean enableSpace;

	private String noOfTabs;

	private String className;

	@PostConstruct
	protected void init() {

		if (null != tabsList) {
			int size = tabsList.size();

			for (int i =0 ; i < size; i++) {
				if (StringUtils.isBlank(tabsList.get(i))) {
					this.tabsList.remove(i);
				}
			}

			noOfTabs = "tabs-" + size;
			className = "col-xs-" + ApplicationConstants.TOTAL_GRIDS / size;
		}
	}

	public String getNoOfTabs() {
		return this.noOfTabs;
	}

	public String getClassName() {
		return this.className;
	}

	public List<String> getTabsList() {
		return this.tabsList;
	}

	public boolean isEnableSpace() {
		return enableSpace;
	}
}
