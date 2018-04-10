package com.mcd.rwd.us.core.bean;

import java.util.List;

/**
 * Created by Rakesh.Balaiah on 19-07-2015.
 */
public class NavItem {

	private String path;

	private String title;

	private List<NavItem> navList;

	private boolean overflows;

	private boolean isActive;

	public String getPath() {
		return path;
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public List<NavItem> getNavList() {
		return navList;
	}

	public void setNavList(final List<NavItem> navList) {
		this.navList = navList;
	}

	public boolean isOverflows() {
		return overflows;
	}

	public void setOverflows(final boolean overflows) {
		this.overflows = overflows;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setIsActive(final boolean isActive) {
		this.isActive = isActive;
	}

}
