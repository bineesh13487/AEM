package com.mcd.rwd.us.core.sightly.callouts;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

/**
 * Created by sandeepc on 04/07/17.
 */

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CalloutStaticRes {

    @DialogField(fieldLabel = "Width*", name = "./width", additionalProperties = {
            @Property(name = "emptyOption", value = "true")
    })
    @Selection(type = Selection.SELECT, options = {
            @Option(text = "~25%", value = "3"),@Option(text = "~33%", value = "4"),@Option(text = "~50%", value = "6")
    })
    @Inject
    private String width;

    @DialogField(fieldLabel = "Link*", name = "./link", fieldDescription = "Please choose scaffolding page path.")
    @PathField(rootPath = "/content")
    @Inject
    private String link;
}
