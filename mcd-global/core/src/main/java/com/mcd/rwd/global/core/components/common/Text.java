package com.mcd.rwd.global.core.components.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.Hidden;
import com.citytechinc.cq.component.annotations.widgets.RichTextEditor;
import com.citytechinc.cq.component.annotations.widgets.rte.*;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by rakesh on 12/06/17.
 */
@Component(value = "Text Component", actions = { "text: text Component", "-", "editannotate", "copymove", "delete" },
        disableTargeting = true, group = "GWS-Global", inPlaceEditingActive = true, inPlaceEditingEditorType = "text",
        inPlaceEditingConfigPath = "../../dialog/items/tabs/items/TextComponent/items/text" , path="/common")
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Text {

    public static final String COMMON_TEXT_COMPONENT = "mcd-rwd-global/components/content/common/text";


    @DialogField(fieldLabel = "Text",
            fieldDescription = "Use the in place text editor to edit this field.",
            name="./text")
    @RichTextEditor(
            edit = @Edit,
            subsuperscript = @SubSuperscript,
            lists = @Lists,
            justify = @Justify,
            misctools = @MiscTools(specialchars = false, sourceedit = true),
            spellcheck = @SpellCheck,
            findreplace = @FindReplace,
            links = @Links,
            paraformat = {
                    @ParaFormat(tag = "h1", description = "Heading 1"),
                    @ParaFormat(tag = "h2", description = "Heading 2"),
                    @ParaFormat(tag = "h3", description = "Heading 3"),
                    @ParaFormat(tag = "h4", description = "Heading 4"),
                    @ParaFormat(tag = "h5", description = "Heading 5"),
                    @ParaFormat(tag = "p", description = "Paragraph")
            })
    @Inject @Named("text")
    private String text;

    @DialogField(name = "./" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, defaultValue = COMMON_TEXT_COMPONENT)
    @Hidden(COMMON_TEXT_COMPONENT)
    private String resourceType;

    public String getText() {
        return text;
    }
}
