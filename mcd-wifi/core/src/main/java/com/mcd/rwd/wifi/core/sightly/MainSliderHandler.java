package com.mcd.rwd.wifi.core.sightly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.Tab;
import com.icfolson.aem.multicompositeaddon.widget.MultiCompositeField;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;
import com.google.gson.Gson;
import com.mcd.rwd.wifi.core.bean.MainSliderBean;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Component(
		name = "main-slider",
		value = "Wifi Main Slider Component",
		actions = { "text: Wifi Main Slider Component", "-", "editannotate", "copymove", "delete" },
		disableTargeting = true,
		tabs = {
				@Tab(title = "Main Slider Configuration" , touchUINodeName = "tab1")
		},
		group = "McD-Wifi")
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MainSliderHandler{

	private static final Logger logger = LoggerFactory.getLogger(MainSliderHandler.class);

	@DialogField(fieldLabel="Add Slider Tiles" , name = "./gallery", fieldDescription="Please enter form categories for different user feedback with their url.",additionalProperties = @Property(name = "class", value = "wifi-main-slider"))
	@MultiCompositeField
	@Inject
	@Named("gallery")
	private List<MainSliderBean> mainSliderList;



	@Inject
	SlingHttpServletRequest request;


	private int handlerLength = 0;
	private String configureMessage = "Please configure the component";
	
	@PostConstruct
	public void activate() throws Exception {
			setMainSliderList(getMainSliderlList(mainSliderList));
	}

	private List<MainSliderBean> getMainSliderlList(List<MainSliderBean> multiFieldList) {
		mainSliderList = new ArrayList<MainSliderBean>();
		int size = 0;
		try{
			MainSliderBean mainSliderBean = null;
			Resource multiCompositeres = request.getResource().getChild("gallery");
			if(multiCompositeres.hasChildren()) {
				size++;
				Iterator<Resource> multiCompositeResourceIterator = multiCompositeres.listChildren();
				while (multiCompositeResourceIterator.hasNext()) {
					Resource itemRes = multiCompositeResourceIterator.next();
					mainSliderBean = new MainSliderBean();
					if (null != itemRes) {
							ValueMap valueMap = itemRes.getValueMap();
							if(null != valueMap){
								if(valueMap.get("tileImage").toString().startsWith("/content")) {
									mainSliderBean.setTileImage(valueMap.get("tileImage").toString().concat(".html"));
								}
								mainSliderBean.setTitle(null != valueMap.get("title")  ? valueMap.get("title").toString() : "");
								mainSliderBean.setSubTitle(null != valueMap.get("subTitle") ? valueMap.get("subTitle").toString() : "");
								mainSliderBean.setBgElement(null != valueMap.get("bgElement") && !valueMap.get("bgElement").toString().isEmpty() ? valueMap.get("bgElement").toString() : "");
								mainSliderBean.setTileImageLinke(null != valueMap.get("tileImageLinke").toString() ? valueMap.get("tileImageLinke").toString() : "");
								mainSliderBean.setBgImage(null != valueMap.get("bgImage") ? valueMap.get("bgImage").toString() : "");
								mainSliderBean.setBgImageAltText(null != valueMap.get("bgImageAltText").toString() ? valueMap.get("bgImageAltText").toString() : "");
								mainSliderBean.setBgVideo(null != valueMap.get("bgVideo") ? valueMap.get("bgVideo").toString() : "");
								mainSliderBean.setVideoType(null != valueMap.get("videoType") ? valueMap.get("videoType").toString() : "");
								mainSliderBean.setTarget(null != valueMap.get("target") ? valueMap.get("target").toString() : "");
								mainSliderBean.setColorPicker(null != valueMap.get("colorPicker") ? valueMap.get("colorPicker").toString() : "");
								mainSliderBean.setYoutubeVideo(null != valueMap.get("youtubeVideo") ? valueMap.get("youtubeVideo").toString() : "");
								mainSliderBean.setPopupVideoPath(null != valueMap.get("popupVideoPath") ? valueMap.get("popupVideoPath").toString() : "");
								mainSliderBean.setTileImageText(null != valueMap.get("tileImageText") ? valueMap.get("tileImageText").toString() : "");
								mainSliderBean.setTileImageAltText(null != valueMap.get("tileImageAltText") ? valueMap.get("tileImageAltText").toString() : "");
								mainSliderBean.setTextAlign(null != valueMap.get("textAlign") ? valueMap.get("textAlign").toString() : "bottom");

							}
						}
						mainSliderList.add(mainSliderBean);
					}

				}
				setHandlerLength(size);


		} catch (Exception e) {
			logger.error("Exception in getMainSliderList method : "+e);
		}
		
		return mainSliderList;
	}

	public int getHandlerLength() {
		return handlerLength;
	}

	public void setHandlerLength(int handlerLength) {
		this.handlerLength = handlerLength;
	}

	public String getConfigureMessage() {
		return configureMessage;
	}

	public void setConfigureMessage(String configureMessage) {
		this.configureMessage = configureMessage;
	}

	public List<MainSliderBean> getMainSliderList() {
		return mainSliderList;
	}

	public void setMainSliderList(List<MainSliderBean> mainSliderList) {
		this.mainSliderList = mainSliderList;
	}

}
