package com.mcd.rwd.global.core.components.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

/**
 * Created by rakesh on 13/06/17.
 */
@Component(value = "Heading Component", actions = { "text: heading Component", "-", "editannotate", "copymove", "delete" },
        disableTargeting = true, group = "GWS-Global" , path="/common")
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Heading {

    @DialogField(fieldLabel = "Text", fieldDescription = "Enter Text", ranking = 2D,
            additionalProperties = {@Property(name = "maxlength", value = "50")})
    @TextField
    @Inject
    String title;

    @DialogField(fieldLabel = "Layout", required = true, ranking = 2D)
    @Selection(options = {
            @Option(text = "Heading 1", value = "h1"),
            @Option(text = "Heading 2", value = "h2"),
            @Option(text = "Heading 3", value = "h3"),
            @Option(text = "Heading 4", value = "h4"),
            @Option(text = "Heading 5", value = "h5"),
            @Option(text = "Heading 6", value = "h6")
            }, type = Selection.SELECT)
    @Inject
    String headingType;

    public String getTitle() {
        return title;
    }

    public String getHeadingType() {
        return headingType;
    }
}
