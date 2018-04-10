package com.mcd.rwd.global.core.bean;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.citytechinc.cq.component.annotations.widgets.Selection;

/**
 * Created by sandeepc on 18/07/17.
 */

public interface YouTubeSettings {

    @DialogField(tab = 3, additionalProperties = @Property(name = "value", value = "true"))
    @CheckBox(text = "Show Controls")
    boolean isControls();

    @DialogField(tab = 3, name = "./fs", additionalProperties = @Property(name = "value", value = "true"))
    @CheckBox(text = "Allow Fullscreen")
    boolean isAllowFullScreen();

    @DialogField(tab = 3, name = "./hd", fieldLabel = "Buffered Video Quality", fieldDescription = "In an attempt " +
            "to provide the best video experience we can buffer the video so that it remains at youtube's highest " +
            "video ratio.",
            additionalProperties = {@Property(name = "emptyOption", value = "true")})
    @Selection(type = Selection.SELECT, options = {
            @Option(text = "Small (240p)", value = "small"), @Option(text = "Medium (360p)", value = "medium"),
            @Option(text = "Large (480p)", value = "large"), @Option(text = "HD (720p)", value = "hd720")
    })
    String getVideoQuality();

    @DialogField(tab = 3, name = "./related", additionalProperties = @Property(name = "value", value = "true"))
    @CheckBox(text = "Show Related Videos")
    boolean isRelated();
}
