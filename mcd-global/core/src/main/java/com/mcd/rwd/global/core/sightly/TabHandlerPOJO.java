package com.mcd.rwd.global.core.sightly;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.global.core.widget.timefield.TimeField;
import com.mcd.rwd.global.core.widget.timefield.Type;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

/**
 * Created by Krupa on 17/07/17.
 */

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TabHandlerPOJO {

    @DialogField(fieldLabel = "Title", required = true)
    @TextField
    @Inject @Default(values = StringUtils.EMPTY)
    private String title;

    @DialogField(fieldLabel = "Start Time", additionalProperties = {@Property(name = "type", value = "time")})
    @TimeField(displayedFormat = TimeField.DEFAULT_TIME_FORMAT, valueFormat = TimeField.DEFAULT_TIME_FORMAT, type = Type.TIME)
    @Inject
    @Default(values = StringUtils.EMPTY)
    private String startTime;

    @DialogField(fieldLabel = "End Time", additionalProperties = {@Property(name = "type", value = "time")})
    @TimeField(displayedFormat = TimeField.DEFAULT_TIME_FORMAT, valueFormat = TimeField.DEFAULT_TIME_FORMAT, type = Type.TIME)
    @Inject
    @Default(values = StringUtils.EMPTY)
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
