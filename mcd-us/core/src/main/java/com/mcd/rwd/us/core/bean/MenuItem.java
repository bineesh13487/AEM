package com.mcd.rwd.us.core.bean;


/**
 * Created by Dipankar Gupta on 25-03-2016.
 */
public class MenuItem {

	private String path;

	private String title;

	private String url;

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

	public void setURL(final String url) { this.url = url; }

	public String getURL() { return url; }

}
