package com.mcd.rwd.us.core.sso_profile.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.Hidden;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.icfolson.aem.library.api.node.ComponentNode;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import com.mcd.rwd.us.core.sso_profile.model.DirectiveInput;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component(
        value = "Address",
        path = SSOProfileConstants.SSO_COMPONENTS_PATH + "/common",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class Address {

    @DialogField(
            fieldLabel = "Include this address",
            name = "./included",
            fieldDescription = "Indicates whether this address should be included on the form.<br/>"
                    + "When an address is included, all its fields are automatically included. Make sure you provide appropriate labels/In-Field Texts.",
            ranking = SSOProfileConstants.RANKING_ZERO
    )
    @Selection(type = "checkbox")
    @Inject @Default(values = "false")
    private boolean included;


    @DialogField(fieldLabel = "Address Line 1", ranking = SSOProfileConstants.RANKING_ONE)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "addressLine1/", title = "Address Line 1 related properties")
    //@ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormFieldNoIncl addressLine1;

    @DialogField (fieldLabel = "Address Line 2", ranking = SSOProfileConstants.RANKING_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "addressLine2/", title = "Address Line 2 related properties")
    //@ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormFieldNoIncl addressLine2;

    @DialogField (fieldLabel = "Address Line 3", ranking = SSOProfileConstants.RANKING_THREE)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "addressLine3/", title = "Address Line 3 related properties")
    //@ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormFieldNoIncl addressLine3;

    @DialogField (fieldLabel = "City", ranking = SSOProfileConstants.RANKING_FOUR)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "city/", title = "City related properties")
    //@ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormFieldNoIncl city;

    @DialogField (fieldLabel = "Postal Code", ranking = SSOProfileConstants.RANKING_FIVE)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "postalCode/", title = "Postal Code related properties")
    //@ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormFieldNoIncl postalCode;

    @DialogField (fieldLabel = "State/Province", ranking = SSOProfileConstants.RANKING_SIX)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "state/", title = "State/Province related properties")
    //@ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormFieldNoIncl state;

    @Inject
    ComponentNode componentNode;

    @DialogField(
            name = "./" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
            defaultValue = SSOProfileConstants.RESOURCE_ADDRESS_COMPONENT)
    @Hidden(value = SSOProfileConstants.RESOURCE_ADDRESS_COMPONENT)
    private String resourceType;

    public boolean isIncluded() {
        return included;
    }

    public TextFormFieldNoIncl getAddressLine1() {
        return addressLine1;
    }

    public TextFormFieldNoIncl getAddressLine2() {
        return addressLine2;
    }

    public TextFormFieldNoIncl getAddressLine3() {
        return addressLine3;
    }

    public TextFormFieldNoIncl getCity() {
        return city;
    }

    public TextFormFieldNoIncl getPostalCode() {
        return postalCode;
    }

    public TextFormFieldNoIncl getState() {
        return state;
    }

    public List<DirectiveInput> getDirectiveInputs() {

        if (isIncluded()) {
            final List<DirectiveInput> inputs = new LinkedList<>();
            final String addressName = componentNode.getResource().getName() + "_";

            if (componentNode.getComponentNode("addressLine1").isPresent()) {
                inputs.add(new DirectiveInput(componentNode.getComponentNode("addressLine1").get()).setName(addressName + "addressLine1"));
            }
            if (componentNode.getComponentNode("addressLine2").isPresent()) {
                inputs.add(new DirectiveInput(componentNode.getComponentNode("addressLine2").get()).setName(addressName + "addressLine2"));
            }
            if (componentNode.getComponentNode("addressLine3").isPresent()) {
                inputs.add(new DirectiveInput(componentNode.getComponentNode("addressLine3").get()).setName(addressName + "addressLine3"));
            }
            if (componentNode.getComponentNode("city").isPresent()) {
                inputs.add(new DirectiveInput(componentNode.getComponentNode("city").get()).setName(addressName + "city"));
            }
            if (componentNode.getComponentNode("postalCode").isPresent()) {
                inputs.add(new DirectiveInput(componentNode.getComponentNode("postalCode").get()).setName(addressName + "postalCode"));
            }
            if (componentNode.getComponentNode("state").isPresent()) {
                inputs.add(new DirectiveInput(componentNode.getComponentNode("state").get()).setName(addressName + "state"));
            }

            return inputs;
        }

        return Collections.emptyList();
    }
}
