package com.mcd.rwd.us.core.sso_profile.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Component(
        value = "Text Form Field",
        path = SSOProfileConstants.SSO_COMPONENTS_PATH + "/common",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class TextFormField extends TextFormFieldNoIncl {

    @DialogField(
            fieldLabel = "Include this field",
            name = "./included",
            fieldDescription = "Check this checkbox if this field should be included on the form.",
            ranking = SSOProfileConstants.RANKING_ZERO
    )
    @Selection(type = "checkbox")
    @Inject @Default(values = "false")
    private boolean included;

//    @DialogField(
//            name = "./" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
//            defaultValue = SSOProfileConstants.RESOURCE_TEXT_FORM_FIELD_COMPONENT)
//    @Hidden(value = SSOProfileConstants.RESOURCE_TEXT_FORM_FIELD_COMPONENT)
//    private String resourceType;

    public boolean isIncluded() {
        return included;
    }
}
