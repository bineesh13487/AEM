package com.mcd.rwd.us.core.bean;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Rakesh.Balaiah on 27-09-2015.
 */
public final class Link {

	private String href;

	private String text;

	private String target;

	private String linkClass;
	
	private String activeLink = StringUtils.EMPTY;

	/**
	 * Returns the link's source.
	 *
	 * @return the href
	 */
	public String getHref() {
		return href;
	}

	/**
	 * Sets the link's source
	 *
	 * @param href The source
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * @return Returns the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text for the link.
	 *
	 * @param text Text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return The target.
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Sets the target for the link.
	 *
	 * @param target
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @return true to show bumper.
	 */
	public String getLinkClass() {
		return linkClass;
	}

	/**
	 * Sets the showBumper.
	 *
	 * @param linkClass
	 */
	public void setLinkClass(String linkClass) {
		this.linkClass = linkClass;
	}

	public String getActiveLink() {
		return activeLink;
	}

	public void setActiveLink(String activeLink) {
		this.activeLink = activeLink;
	}
}
