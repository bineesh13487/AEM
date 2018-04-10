package com.mcd.rwd.global.core.bean;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;

/**
 * Created by sandeepc on 18/07/17.
 */

public interface VideoProps {

    @DialogField(name = "./autoplay", additionalProperties = @Property(name = "value", value = "true"))
    @CheckBox(text = "Autoplay")
    boolean isAutoplay();

    @DialogField(name =  "./loop", additionalProperties = @Property(name = "value", value = "true"))
    @CheckBox(text = "Loop")
    boolean isLoop();

}
