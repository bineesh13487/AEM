package com.mcd.rwd.us.core.sightly.callouts;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.mcd.rwd.global.core.widget.timefield.TimeField;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by sandeepc on 04/07/17.
 */

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CallOutPromo {

    @DialogField(fieldLabel = "Link*")
    @PathField
    @Inject
    private String link;

    @DialogField(fieldLabel = "Start Time", additionalProperties = { @Property(name = "type", value = "time"),
            @Property(name = "typeHint", value = "String") })
    @TimeField(displayedFormat = "hh:mm A", valueFormat = "hh:mm A")
    @Inject
    private String startTime;

    @DialogField(fieldLabel = "End Time", additionalProperties = { @Property(name = "type", value = "time"),
            @Property(name = "typeHint", value = "String") })
    @TimeField(displayedFormat = "hh:mm A", valueFormat = "hh:mm A")
    @Inject
    private String endTime;

    @DialogField(fieldLabel = "Default", name = "default", additionalProperties = {
            @Property(name = "value", value = "true")
    })
    @CheckBox(text = "Default")
    @Inject @Named("default")
    private Boolean defualt;
}
