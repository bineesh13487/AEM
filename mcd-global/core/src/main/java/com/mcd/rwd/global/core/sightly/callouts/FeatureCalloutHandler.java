package com.mcd.rwd.global.core.sightly.callouts;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.icfolson.aem.multicompositeaddon.widget.MultiCompositeField;
import com.mcd.rwd.global.core.bean.FeatureCallout;
import com.mcd.rwd.global.core.constants.CalloutConstants;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Handler class for Feature Callout Component.
 *
 * @author HCL
 */

@Component(
		name = "featurecallout",
		value = "Feature Callout",
		description = "Displays the promotional callouts with day parting ability.",
		disableTargeting = true,
		resourceSuperType = "foundation/components/parbase",
		group = ".hidden",
		listeners = {
				@Listener(name = "afteredit", value = "REFRESH_PAGE")
		},
		tabs = {
				@Tab(title = "General Settings", touchUINodeName = "general"),
				@Tab(title = "Callout 1", touchUINodeName = "callout1"),
				@Tab(title = "Callout 2", touchUINodeName = "callout2"),
				@Tab(title = "Callout 3", touchUINodeName = "callout3"),
				@Tab(title = "Callout 4", touchUINodeName = "callout4"),
				@Tab(title = "Callout 1", touchUINodeName = "static1"),
				@Tab(title = "Callout 2", touchUINodeName = "static2"),
				@Tab(title = "Callout 3", touchUINodeName = "static3"),
				@Tab(title = "Callout 4", touchUINodeName = "static4")
		}
)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FeatureCalloutHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeatureCalloutHandler.class);

	@DialogField(fieldLabel = "No. of Callouts", fieldDescription = "Number of Callouts to be displayed on the page.",
			additionalProperties = {@Property(name = "emptyOption", value = "true"),
					@Property(name = "class", value = "feature-callout-number global")})
	@Selection(type = Selection.SELECT, options = {@Option(text = "3", value = "3"), @Option(text = "4", value = "4")})
	private int number;

	@DialogField(name = "./type", fieldLabel = "Type", fieldDescription = "'Static' - Only one Callout can be configured for a " +
			"particular place.</br>" +
			"'Multiple (Random)' - Multiple Callout can be configured which will be displayed randomly within " +
			"each callout area.</br>" +
			"'Multiple (Time Sensitive)'- Multiple Callout can be configured for different time slots.",
			additionalProperties = {@Property(name = "emptyOption", value = "true"),
					@Property(name = "class", value = "feature-callout-type global")})
	@Selection(type = Selection.SELECT, options = { @Option(text = "Static", value = "static"),
			@Option(text = "Multiple (Random)", value = "random"),
			@Option(text = "Multiple (Time Sensitive)", value = "time")
	})
	@Inject @Named("type")
	private String calloutType;

	@DialogField(tab = 2, additionalProperties = @Property(name = "class", value = "feature-callout"))
	@MultiCompositeField
	private List<FeatureCallout> callout1;

	@DialogField(tab = 3, additionalProperties = @Property(name = "class", value = "feature-callout"))
	@MultiCompositeField
	private List<FeatureCallout> callout2;

	@DialogField(tab = 4, additionalProperties = @Property(name = "class", value = "feature-callout"))
	@MultiCompositeField
	private List<FeatureCallout> callout3;

	@DialogField(tab = 5, additionalProperties = @Property(name = "class", value = "feature-callout"))
	@MultiCompositeField
	private List<FeatureCallout> callout4;

	@DialogField(tab = 6, additionalProperties = @Property(name = "class", value = "feature-static"))
	@DialogFieldSet(namePrefix = "./static1/")
	private FeatureCallout static1;

	@DialogField(tab = 7, additionalProperties = @Property(name = "class", value = "feature-static"))
	@DialogFieldSet(namePrefix = "./static2/")
	private FeatureCallout static2;

	@DialogField(tab = 8, additionalProperties = @Property(name = "class", value = "feature-static"))
	@DialogFieldSet(namePrefix = "./static3/")
	private FeatureCallout static3;

	@DialogField(tab = 9, additionalProperties = @Property(name = "class", value = "feature-static"))
	@DialogFieldSet(namePrefix = "./static4/")
	private FeatureCallout static4;

	private boolean isEmpty;

	@Inject
	@Via("request")
	Resource resource;

	/* (non-Javadoc)
	 * @see com.adobe.cq.sightly.WCMUse#activate()
	 */
	@PostConstruct
	protected void init() {
		if (null != resource) {
			LOGGER.debug("**** Resource exists === "+resource.getPath()+" ****");
			//Check whehther the Component is configured or not
			if (CalloutConstants.TYPE_STATIC.equals(calloutType)) {
				//Check for the static type
				Resource calloutResource = resource.getChild(CalloutConstants.FIRST_STATIC_RESOURCE);
				if (null != calloutResource) {
					String calloutLink = ResourceUtil
							.getChildResourceProperty(resource, CalloutConstants.FIRST_STATIC_RESOURCE,
									CalloutConstants.PROPERTY_STATIC_LINK);
					if (StringUtils.isBlank(calloutLink)) {
						this.isEmpty = true;
					}
				}
			} else {
				//Check for random and time-based values
				Resource calloutResource = resource.getChild(CalloutConstants.FIRST_DYNAMIC_RESOURCE);
				if (null != calloutResource && calloutResource.hasChildren()) {
					Iterable<Resource> children = calloutResource.getChildren();
					for (Resource resource : children) {
						String calloutLink = resource.getValueMap().
								get(CalloutConstants.PROPERTY_STATIC_LINK, StringUtils.EMPTY);
						if (StringUtils.isBlank(calloutLink)) {
							this.isEmpty = true;
						}
					}
				}
				else {
					this.isEmpty = true;
				}
			}
		}
		else {
			LOGGER.debug("**** Resource is NULL something wrong ****");
		}
	}

	/**
	 * @return the calloutType
	 */
	public String getCalloutType() {
		return calloutType;
	}

	/**
	 * @param calloutType the calloutType to set
	 */
	public void setCalloutType(String calloutType) {
		this.calloutType = calloutType;
	}

	/**
	 * @return the isEmpty
	 */
	public boolean isEmpty() {
		return isEmpty;
	}

	/**
	 * @param isEmpty the isEmpty to set
	 */
	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

}
