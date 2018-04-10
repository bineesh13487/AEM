package com.mcd.rwd.us.core.sightly.callouts;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import com.mcd.rwd.us.core.constants.PromoConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Handler class for Feature Callout Component.
 *
 * @author HCL
 */

@Component(
        name = "featurecallout",
        value = "Feature Callout",
        description = "Displays the promotional callouts with day parting ability.",
        resourceSuperType = "foundation/components/parbase",
        group = " GWS-Global",
        disableTargeting = true,
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
                @Tab(title = "Callout 4", touchUINodeName = "static4"),
                @Tab(title = "Callout 1", touchUINodeName = "random1"),
                @Tab(title = "Callout 2", touchUINodeName = "random2"),
                @Tab(title = "Callout 3", touchUINodeName = "random3"),
                @Tab(title = "Callout 4", touchUINodeName = "random4")
        }

)
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FeatureCalloutHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FeatureCalloutHandler.class);

    @DialogField(tab = 1, fieldLabel = "No. of Callouts", fieldDescription = "Number of Callouts " +
            "to be displayed on the page.", additionalProperties = {
            @Property(name = "class", value = "feature-callout-number"),
            @Property(name = "emptyOption", value = "true") }, listeners = {
            @Listener(name = "selectionchanged", value = "function(box, value) {\n" +
                    "\tvar tabPanel = box.findParentByType('tabpanel');\n" +
                    "\tvar dialog = box.findParentByType('dialog');\n" +
                    "\tvar type = dialog.getField('./type').getValue();\n" +
                    "\ttabPanel.hideAllTabs();\n" +
                    "\tvar tabType = 'static';\n" +
                    "\t\n" +
                    "\tif(type!='static' && type!='random') {\n" +
                    "\t\ttabType = 'callout';\n" +
                    "\t}\n" +
                    "\telse if(type!='static' && type!='time'){\n" +
                    "\t\ttabType = 'random';\n" +
                    "\t}\n" +
                    "\t\n" +
                    "\tvar tabs = [];\n" +
                    "\tif(type != '') {\n" +
                    "\t\tfor(var i=1; i<=value; i++) {\n" +
                    "\t\t\ttabs.push(tabType+i);\n" +
                    "\t\t}\n" +
                    "\t}\n" +
                    "\ttabPanel.manageTabs(tabs,true);\n" +
                    "}")
    })
    @Selection(type = Selection.SELECT, options = {
            @Option(text = "3", value = "3"), @Option(text = "4", value = "4")
    })
    @Inject
    private String number;

    @DialogField(tab = 1, fieldLabel = "Type", fieldDescription = "'Multiple (Time Sensitive) '- " +
            "Multiple Callout can be configured for different time slots.", name = "./type", additionalProperties = {
                @Property(name = "class", value = "feature-callout-type"),
                @Property(name = "emptyOption", value = "true") }, listeners = {
                @Listener(name = "loadcontent", value = "function(){\n" +
                    "\tvar type = this.getValue();\n" +
                    "\tvar tabPanel = this.findParentByType('tabpanel');\n" +
                    "\tvar dialog = this.findParentByType('dialog');\n" +
                    "\tvar number = dialog.getField('./number').getValue();\n" +
                    "\ttabPanel.hideAllTabs();\n" +
                    "\tvar tabType = 'static';\n" +
                    "\tif(type!='static' && type!='random') {\n" +
                    "\t\ttabType = 'callout';\n" +
                    "\t}\n" +
                    "\telse if(type!='static' && type!='time'){\n" +
                    "\t\ttabType = 'random';\n" +
                    "\t}\n" +
                    "\t\n" +
                    "\tvar tabs = [];\n" +
                    "\tfor(var i=1; i<=number; i++) {\n" +
                    "\t\ttabs.push(tabType+i);\n" +
                    "\t}\n" +
                    "\ttabPanel.manageTabs(tabs,true);\n" +
                    "}"),
            @Listener(name = "selectionchanged", value = "function(box,value){\n" +
                    "\tvar tabPanel = box.findParentByType('tabpanel');\n" +
                    "\tvar dialog = box.findParentByType('dialog');\n" +
                    "\tvar number = dialog.getField('./number').getValue();\n" +
                    "\ttabPanel.hideAllTabs();\n" +
                    "\tvar tabType = 'static';\n" +
                    "\tif(value!='static' && value!='random') {\n" +
                    "\t\ttabType = 'callout';\n" +
                    "\t}\n" +
                    "\telse if(value!='static' && value!='time'){\n" +
                    "\t\ttabType = 'random';\n" +
                    "\t}\n" +
                    "\t\n" +
                    "\tvar tabs = [];\n" +
                    "\tfor(var i=1; i<=number; i++) {\n" +
                    "\t\ttabs.push(tabType+i);\n" +
                    "\t}\n" +
                    "\ttabPanel.manageTabs(tabs,true);\n" +
                    "}")
    })
    @Selection(type = Selection.SELECT, options = {
            @Option(text = "Static", value = "static"), @Option(text = "Multiple (Random)", value = "random"),
            @Option(text = "Multiple (Time Sensitive)", value = "time")
    })
    @Inject @Named("type")
    private String calloutType;

    @DialogField(tab = 2)
    @DialogFieldSet(namePrefix = "callout1/")
    @ChildResource(name = "callout1")
    @Inject
    private CalloutRes calloutRes1;

    @DialogField(tab = 3)
    @DialogFieldSet(namePrefix = "callout2/")
    @ChildResource(name = "callout2")
    @Inject
    private CalloutRes calloutRes2;

    @DialogField(tab = 4)
    @DialogFieldSet(namePrefix = "callout3/")
    @ChildResource(name = "callout3")
    @Inject
    private CalloutRes calloutRes3;

    @DialogField(tab = 5)
    @DialogFieldSet(namePrefix = "callout4/")
    @ChildResource(name = "callout4")
    @Inject
    private CalloutRes calloutRes5;

    @DialogField(tab = 6)
    @DialogFieldSet(namePrefix = "static1/")
    @ChildResource(name = "static1")
    @Inject
    private CalloutStaticRes staticRes1;

    @DialogField(tab = 7)
    @DialogFieldSet(namePrefix = "static2/")
    @ChildResource(name = "static2")
    @Inject
    private CalloutStaticRes staticRes2;


    @DialogField(tab = 8)
    @DialogFieldSet(namePrefix = "static3/")
    @ChildResource(name = "static3")
    @Inject
    private CalloutStaticRes staticRes3;


    @DialogField(tab = 9)
    @DialogFieldSet(namePrefix = "static4/")
    @ChildResource(name = "static4")
    @Inject
    private CalloutStaticRes staticRes4;

    @DialogField(tab = 10)
    @DialogFieldSet(namePrefix = "random1/")
    @ChildResource(name = "random1")
    @Inject
    private CalloutRandomRes randomRes1;

    @DialogField(tab = 11)
    @DialogFieldSet(namePrefix = "random2/")
    @ChildResource(name = "random2")
    @Inject
    private CalloutRandomRes randomRes2;

    @DialogField(tab = 12)
    @DialogFieldSet(namePrefix = "random3/")
    @ChildResource(name = "random3")
    @Inject
    private CalloutRandomRes randomRes3;

    @DialogField(tab = 13)
    @DialogFieldSet(namePrefix = "random4/")
    @ChildResource(name = "random4")
    @Inject
    private CalloutRandomRes randomRes4;

    @Self
    Resource resource;

    private boolean isEmpty;

    /* (non-Javadoc)
     * @see com.adobe.cq.sightly.WCMUse#activate()
     */
    @PostConstruct
    public void activate() {
        LOG.debug("activate() method called.");
        LOG.debug("Check whehther the Component is configured or not");
        //Check whehther the Component is configured or not
        if (PromoConstants.TYPE_STATIC.equals(calloutType)) {
            //Check for the static type
            LOG.debug("Check for the static type");
            Resource calloutResource = resource.getChild(PromoConstants.FIRST_STATIC_RESOURCE);
            if (null != calloutResource) {
                String calloutLink = ResourceUtil
                        .getChildResourceProperty(resource, PromoConstants.FIRST_STATIC_RESOURCE,
                                PromoConstants.PROPERTY_STATIC_LINK);
                this.isEmpty = StringUtils.isBlank(calloutLink) ? true : false;
            }
        } else {
            //Check for random and time-based values
            LOG.debug("Check for random and time-based values");
            this.isEmpty = (StringUtils.isEmpty(this.calloutType) || "static".equals(this.calloutType)) ? true : false;
        }
    }

    /**
     * @return the calloutType
     */
    public String getCalloutType() {
        return calloutType;
    }

    /**
     * @return the isEmpty
     */
    public boolean isEmpty() {
        return isEmpty;
    }

}
