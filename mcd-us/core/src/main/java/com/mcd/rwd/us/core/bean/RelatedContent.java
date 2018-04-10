package com.mcd.rwd.us.core.bean;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import com.mcd.rwd.global.core.bean.Link;

@Model(adaptables=Resource.class)
public class RelatedContent {
	
	private String hideFlag;
	private String imagePath;
	private String heading;
	private String description;
	private String legalText;
	private String ctaTarget;
	private String ariaLAbel;
	private String ctaButtonText;
	private Link ctaButtonLink;
	private String imageAriaLabel;
	
	public String getHideFlag() {
		return hideFlag;
	}
	public void setHideFlag(String hideFlag) {
		this.hideFlag = hideFlag;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getHeading() {
		return heading;
	}
	public void setHeading(String heading) {
		this.heading = heading;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLegalText() {
		return legalText;
	}
	public void setLegalText(String legalText) {
		this.legalText = legalText;
	}

	public String getAriaLAbel() {
		return ariaLAbel;
	}
	public void setAriaLAbel(String ariaLAbel) {
		this.ariaLAbel = ariaLAbel;
	}
	
	public String getCtaButtonText() {
		return ctaButtonText;
	}
	public void setCtaButtonText(String ctaButtonText) {
		this.ctaButtonText = ctaButtonText;
	}
	public Link getCtaButtonLink() {
		return ctaButtonLink;
	}
	public void setCtaButtonLink(com.mcd.rwd.global.core.bean.Link link) {
		this.ctaButtonLink = link;
	}
	public String getCtaTarget() {
		return ctaTarget;
	}
	public void setCtaTarget(String ctaTarget) {
		this.ctaTarget = ctaTarget;
	}
	
	public String getImageAriaLabel() {
		return imageAriaLabel;
	}
	public void setImageAriaLabel(String imageAriaLabel) {
		this.imageAriaLabel = imageAriaLabel;
	}

}
