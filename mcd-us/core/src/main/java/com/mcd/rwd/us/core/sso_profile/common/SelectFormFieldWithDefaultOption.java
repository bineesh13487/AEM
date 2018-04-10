package com.mcd.rwd.us.core.sso_profile.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Component(
        value = "Select Form Field With Default Option",
        path = SSOProfileConstants.SSO_COMPONENTS_PATH + "/common",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class SelectFormFieldWithDefaultOption extends SelectFormField {

    @DialogField(
            fieldLabel = "Default Option Label",
            name = "./defaultOptionLabel",
            fieldDescription = "Label to use for the default option (with empty value). That option will be pre-selected "
                    + "in the drop down when no value is present.",
            additionalProperties = @Property(name = "emptyText", value = "Select"),
            ranking = SSOProfileConstants.RANKING_FOUR)
    @TextField
    @Inject @Default(values = "Select")
    private String defaultOptionLabel;

    public String getDefaultOptionLabel() {
        return defaultOptionLabel;
    }
}
