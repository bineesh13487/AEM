
package com.mcd.rwd.global.core.bean.html;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.*;
import com.mcd.rwd.global.core.bean.Link;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by karan-k on 3/15/2016.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Image {
	@DialogField(fieldLabel = "Image")
	@Html5SmartImage(tab = false , allowUpload = false)
	@Inject
	private String src;

	@DialogField( fieldLabel = "Title", fieldDescription = "Title for the Image")
	@TextField
	@Inject
	private String title;


	@DialogField( fieldLabel = "Sub Title", fieldDescription = "Sub Title for the Image")
	@TextField
	@Inject
	private String subTitle;

	@DialogField( fieldLabel = "Url", fieldDescription = "Path to redirect to")
	@PathField
	@Inject
	private String link;

	@DialogField( fieldLabel = "Tags", fieldDescription = "tags")
	@TagInputField
	@Inject
	private String tag;
	

	private boolean isLocalUrl;
	
	private String alt;

	private String header;
	

	
	private Link bumperLink;

	public Link getBumperLink() {
		return bumperLink;
	}

	public void setBumperLink(Link bumperLink) {
		this.bumperLink = bumperLink;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isLocalUrl() {
		return isLocalUrl;
	}

	public void setLocalUrl(boolean isLocalUrl) {
		this.isLocalUrl = isLocalUrl;
	}

	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}
	
	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
}