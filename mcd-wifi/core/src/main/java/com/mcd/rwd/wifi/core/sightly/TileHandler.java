package com.mcd.rwd.wifi.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.icfolson.aem.multicompositeaddon.widget.MultiCompositeField;
import com.mcd.rwd.wifi.core.bean.TileComponentBean;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Component(
		name = "wifi-tile",
		value = "Wifi Tile Component",
		actions = {"text: Tile Component","-","edit","copymove","delete","insert"},
		disableTargeting = true,
		resourceSuperType = "foundation/components/parbase",
		group = "McD-Wifi",
		tabs = @Tab(title = "Tile Configuration", touchUINodeName = "tab1", listeners = {
				@Listener(name = "beforesubmit", value = "function(dialog) {\n" +
						"    var columns = dialog.getField('./tilelist').getValue();\n" +
						"    var tileList = new Array();\n" +
						"    var flag = true;\n" +
						"    for (i = 0; i < columns.length; i++) {\n" +
						"        var jsonValue = jQuery.parseJSON(columns[i]);\n" +
						"        if (tileList.length != 0) {\n" +
						"            var val = $.inArray(jsonValue.daypartType, tileList);\n" +
						"            if (val > -1) {\n" +
						"                CQ.Ext.Msg.alert('Error', 'Daypart should be unique.');\n" +
						"                flag = false;\n" +
						"                break;\n" +
						"            } else {\n" +
						"                tileList.push(jsonValue.daypartType);\n" +
						"            }\n" +
						"        } else {\n" +
						"            tileList.push(jsonValue.daypartType);\n" +
						"        }\n" +
						"    }\n" +
						"    if (!flag) {\n" +
						"        return false;\n" +
						"    }\n" +
						"}"),
		}),
		listeners = {
				@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "beforesubmit", value = "function(dialog) {\n" +
						"    var columns = dialog.getField('./tilelist').getValue();\n" +
						"    var tileList = new Array();\n" +
						"    var flag = true;\n" +
						"    for (i = 0; i < columns.length; i++) {\n" +
						"        var jsonValue = jQuery.parseJSON(columns[i]);\n" +
						"        if (tileList.length != 0) {\n" +
						"            var val = $.inArray(jsonValue.daypartType, tileList);\n" +
						"            if (val > -1) {\n" +
						"                CQ.Ext.Msg.alert('Error', 'Daypart should be unique.');\n" +
						"                flag = false;\n" +
						"                break;\n" +
						"            } else {\n" +
						"                tileList.push(jsonValue.daypartType);\n" +
						"            }\n" +
						"        } else {\n" +
						"            tileList.push(jsonValue.daypartType);\n" +
						"        }\n" +
						"    }\n" +
						"    if (!flag) {\n" +
						"        return false;\n" +
						"    }\n" +
						"}"),
				@Listener(name = "beforeedit", value = "function(comp) {\n" +
						"    setTimeout(function() {\n" +
						"        var dialog = comp['dialogs']['EDIT'];\n" +
						"        var daypartType = dialog.getField('./daypartType');\n" +
						"        daypartType.disable();\n" +
						"        daypartType.hide();\n" +
						"    }, 100);\n" +
						"}")
		}
)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TileHandler { //extends WCMUsePojo{

	private static final Logger logger = LoggerFactory.getLogger(TileHandler.class);
	private int handlerLength = 0;
	private List<TileComponentBean> tileList;

	@DialogField
	@DialogFieldSet(collapsible = true, title="Default Tile", namePrefix = "tileComponentBean/")
	@ChildResource(name = "tileComponentBean")
	@Inject
	private TileComponentBean tileComponentBean;

	@DialogField(name = "./tilelist", fieldLabel = "Daypart Configuration")
	@MultiCompositeField
	@Named("tilelist")
	@Inject
	private List<TileComponentBean> tileComponentBeanList;

	@Inject
	Resource resource;

	@PostConstruct
	public void activate() {
		//ValueMap properties = getProperties();
		//TileComponentBean tileComponentBean = resource.adaptTo(TileComponentBean.class);
		if (null != tileComponentBean) {
			String defaultImagePath = tileComponentBean.getImagePath();
			//String[] multiFieldList = properties.get("tilelist", String[].class);
			if (null != defaultImagePath){
				List<TileComponentBean> tempTileList = new ArrayList<TileComponentBean>();
				setHandlerLength(1);
				addExtensionImageUrl(tileComponentBean);
				tempTileList.add(tileComponentBean);
				if (null != tileComponentBeanList && tileComponentBeanList.size() > 0) {
					getTileDetails(tileComponentBeanList, tempTileList);
				}
				setTileList(tempTileList);
			}
		}
	}
	
	/*public TileComponentBean getDefaultTile(ValueMap properties){
		TileComponentBean defaultTile = new TileComponentBean();
		defaultTile.setTitle(properties.get("defaultTitle", String.class));
		defaultTile.setColorpicker(properties.get("defaultColorpicker", String.class));
		String textAlign = properties.get("defaultTextAlign", String.class);
		defaultTile.setTextalign(null != textAlign && !textAlign.isEmpty() ? textAlign : "top");
	
		defaultTile.setImagePath(properties.get("defaultImagePath", String.class));
		String imageUrl = properties.get("defaultImageUrl", String.class);
		if(null != imageUrl && !imageUrl.isEmpty() && imageUrl.startsWith("/content")){
			defaultTile.setImageurl(imageUrl.concat(".html"));
		}
		else {
			defaultTile.setImageurl(imageUrl);
		}
		defaultTile.setAlttext(properties.get("defaultAltText", String.class));
		String targetValue = properties.get("defaultTarget", String.class);
		defaultTile.setTarget(null != targetValue && !targetValue.isEmpty() ? targetValue : "modal");
		defaultTile.setDaypartType("default");
		
		return defaultTile;
	}*/
	
	public void getTileDetails(List<TileComponentBean> multiFieldList, List<TileComponentBean> tempTileList){
		for (TileComponentBean field : multiFieldList) {
			addExtensionImageUrl(field);
			tempTileList.add(field);
		}
	}

	/*private TileComponentBean getTileMap (JSONObject jsonObj) {
		TileComponentBean tile = new TileComponentBean();
		try {
			tile.setTitle((String) jsonObj.get("title"));
			tile.setColorpicker((String) jsonObj.get("colorpicker"));
			String textAlign = (String) jsonObj.get("textalign");
			tile.setTextalign(null != textAlign && !textAlign.isEmpty() ? textAlign : "top");
		
			tile.setImagePath((String) jsonObj.get("imagePath"));
			String imageUrl = (String) jsonObj.get("imageurl");
			if(null != imageUrl && !imageUrl.isEmpty() && imageUrl.startsWith("/content")){
				tile.setImageurl(imageUrl.concat(".html"));
			}
			else {
				tile.setImageurl(imageUrl);
			}
			tile.setAlttext((String) jsonObj.get("alttext"));
			String targetValue = (String) jsonObj.get("target");
			tile.setTarget(null != targetValue && !targetValue.isEmpty() ? targetValue : "modal");
			tile.setDaypartType((String) jsonObj.get("daypartType"));
		} catch (JSONException e) {
			logger.error("Exception in getTileMap method : "+e);
		}		
		return tile;
	}*/

	public void addExtensionImageUrl(TileComponentBean tileComponentBean) {
		if (null != tileComponentBean.getImageurl()) {
			String imageUrl = tileComponentBean.getImageurl();
			if (imageUrl.startsWith("/content")) {
				tileComponentBean.setImageurl(imageUrl.concat(".html"));
			}
		}
	}

	public int getHandlerLength() {
		return handlerLength;
	}

	public void setHandlerLength(int handlerLength) {
		this.handlerLength = handlerLength;
	}

	public List<TileComponentBean> getTileList() {
		return tileList;
	}

	public boolean isEmpty() {
		return tileList == null || tileList.isEmpty();
	}

	public void setTileList(List<TileComponentBean> tileList) { this.tileList = tileList; }
}
