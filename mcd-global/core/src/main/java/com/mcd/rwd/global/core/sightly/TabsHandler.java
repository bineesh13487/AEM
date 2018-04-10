package com.mcd.rwd.global.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.icfolson.aem.multicompositeaddon.widget.MultiCompositeField;
import com.mcd.rwd.global.core.bean.Tab;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rakesh.Balaiah on 13-05-2016.
 */
@Component(name = "tabs", value = "Tab Panel",
	disableTargeting = true,
	resourceSuperType = "foundation/components/parbase",
	allowedParents = "[*/parsys]",
	actions = { "text: Tabs", "-", "editannotate", "copymove", "delete" },
	group = " GWS-Global", path = "content",
	listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
			@Listener(name = "afterinsert", value = "REFRESH_PAGE")
	})
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TabsHandler {

	@DialogField(fieldLabel = "Tab Names", fieldDescription = "Please enter the name of the tab.",
			name = "./tabs")
	@MultiCompositeField(limit = 4)
	@Inject
	@Named("tabs")
	private List<TabHandlerPOJO> tabComponent;

	@DialogField(fieldDescription = "Overlaps the tab component on the component preceding it.",
		name = "./enableSpace", fieldLabel = "Overlap with the previous component",
		additionalProperties = {@Property(name = "value", value = "true"),
			@Property(name = "inputValue", value = "true")})
	@CheckBox(text = "Overlap with the previous component")
	@Inject @Default(values = "false") @Named("enableSpace")
	private String bottomspace;

	private static final String PN_ENABLE_SPACE = "enableSpace";
	private static final String PN_START_TIME = "startTime";
	private static final String PN_END_TIME = "endTime";
	private List<Tab> tabsList;

	private String enableSpace;

	private String noOfTabs;

	private String className;

	@PostConstruct
	public void activate() {
		this.enableSpace = this.bottomspace;

		if (null != tabComponent && !tabComponent.isEmpty()) {
			tabsList = new ArrayList<Tab>();

			for (TabHandlerPOJO map : tabComponent) {
				Tab tab = new Tab();
				tab.setTitle(map.getTitle());
				if(null!=map.getStartTime())
				tab.setStartTime(map.getStartTime().toString());
				if(null!=map.getEndTime())
				tab.setEndTime(map.getEndTime().toString());
				tabsList.add(tab);
			}

			int size = tabsList.size();

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

	public List<Tab> getTabsList() {
		return this.tabsList;
	}

	public String getEnableSpace() {
		return enableSpace;
	}
}