package com.mcd.rwd.us.core.bean.search;

import java.util.List;

/**
 * Created by Seema Pandey on 26-05-2016.
 */
public class Match {

	private List<String> description;

	private List<String> title;

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(final List<String> description) {
		this.description = description;
	}

	public List<String> getTitle() {
		return title;
	}

	public void setTitle(final List<String> title) {
		this.title = title;
	}
}
