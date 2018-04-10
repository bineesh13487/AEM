package com.mcd.rwd.us.core.sightly;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcd.rwd.global.core.bean.Link;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.sightly.McDUse;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.ImageUtil;
import com.mcd.rwd.us.core.bean.RelatedContent;
import com.mcd.rwd.us.core.sightly.forms.UKForm;

/**
 * Created by Brijesh on 06/07/2017.
 */

public class RelatedContentHandler extends McDUse {
	private static final Logger LOGGER = LoggerFactory.getLogger(RelatedProductsHandler.class);

	List<RelatedContent> relatedContentList = new ArrayList<RelatedContent>();
	private String relatedContentTitle;
	private String titleColor;
	private String titleAlignment;
	private String textAlignment;
	private String backGroundColor;
	private String ctaButtonColor;
	private String listsize = "0" ;
	private String gutterspace;

	@Override
	public void activate() throws Exception {
		BumperUtil bumperUtil = new BumperUtil(getSiteConfigResource(ApplicationConstants.RES_BUMPER));
		LOGGER.debug("Start of RelatedContentHandler method");
		UKForm ukForm = new UKForm();
		ValueMap properties = getProperties();
		RelatedContent relatedCont1 = new RelatedContent();
		RelatedContent relatedCont2 = new RelatedContent();
		relatedCont1.setHideFlag(properties.get("hideflag", null));
		Resource sect1Resource = getResource().getChild("firstimage");
		String sect1imageHref = ImageUtil.getImagePath(sect1Resource);
		relatedCont1.setHeading(properties.get("heading", null));

		relatedCont2.setHideFlag(properties.get("sechideflag", null));
		Resource sect2Resource = getResource().getChild("secondimage");
		String sect2imageHref = ImageUtil.getImagePath(sect2Resource);
		relatedCont2.setHeading(properties.get("secheading", null));
		//Retriving Section one values
		if (relatedCont1.getHideFlag() == null && sect1imageHref != null) {
			Link link1 = bumperUtil.getLink(properties.get("ctaLink", ""),properties.get("ctaText", ""),properties.get("linkTarget",false));
			relatedCont1.setImagePath(sect1imageHref);
			relatedCont1.setAriaLAbel(properties.get("ariaCta", ""));
			relatedCont1.setCtaButtonText(properties.get("ctaText", ""));
			relatedCont1.setCtaButtonLink(link1);
			relatedCont1.setDescription(ukForm.tagRemoval(properties.get("description", "")));
			relatedCont1.setLegalText(ukForm.tagRemoval(properties.get("legaltext", "")));
			relatedCont1.setImageAriaLabel(properties.get("imageariaCta", ""));
			relatedContentList.add(relatedCont1);
		}
		//Retriving Section one values
		if (relatedCont2.getHideFlag() == null && sect2imageHref != null) {
			Link link2 = bumperUtil.getLink(properties.get("secctaLink", ""),properties.get("secctaText", ""),properties.get("seclinkTarget",false));
			relatedCont2.setImagePath(sect2imageHref);
			relatedCont2.setAriaLAbel(properties.get("secariaCta", ""));
			relatedCont2.setCtaButtonText(properties.get("secctaText", ""));
			relatedCont2.setCtaButtonLink(link2);
			relatedCont2.setDescription(ukForm.tagRemoval(properties.get("secdescription", "")));
			relatedCont2.setLegalText(ukForm.tagRemoval(properties.get("seclegaltext", "")));
			relatedCont2.setImageAriaLabel(properties.get("secimageariaCta", ""));
			relatedContentList.add(relatedCont2);
		}
		listsize = String.valueOf(relatedContentList.size()); 
		relatedContentTitle = properties.get("relatedtitle", "");
		titleColor = "#"+properties.get("titlecolor", "000000");
		titleAlignment = properties.get("titleAlign","left");
		textAlignment =  properties.get("textalign","left"); 
		backGroundColor = "#"+properties.get("backgroundcolor","F1F1EB");
		ctaButtonColor =  "#"+properties.get("ctabuttoncolor","BF0C0C");
		gutterspace = properties.get("gutterspace","false");
		
		LOGGER.debug("End of RelatedContentHandler method");

	}
	
	public List<RelatedContent> getRelatedContentList() {
		return relatedContentList;
	}

	public void setRelatedContentList(List<RelatedContent> relatedContentList) {
		this.relatedContentList = relatedContentList;
	}

	public String getRelatedContentTitle() {
		return relatedContentTitle;
	}

	public void setRelatedContentTitle(String relatedContentTitle) {
		this.relatedContentTitle = relatedContentTitle;
	}

	public String getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	public String getTitleAlignment() {
		return titleAlignment;
	}

	public void setTitleAlignment(String titleAlignment) {
		this.titleAlignment = titleAlignment;
	}

	public String getTextAlignment() {
		return textAlignment;
	}

	public void setTextAlignment(String textAlignment) {
		this.textAlignment = textAlignment;
	}

	public String getBackGroundColor() {
		return backGroundColor;
	}

	public void setBackGroundColor(String backGroundColor) {
		this.backGroundColor = backGroundColor;
	}

	public String getCtaButtonColor() {
		return ctaButtonColor;
	}

	public void setCtaButtonColor(String ctaButtonColor) {
		this.ctaButtonColor = ctaButtonColor;
	}

	public String getListsize() {
		return listsize;
	}

	public void setListsize(String listsize) {
		this.listsize = listsize;
	}

	public String getGutterspace() {
		return gutterspace;
	}

	public void setGutterspace(String gutterspace) {
		this.gutterspace = gutterspace;
	}

}
