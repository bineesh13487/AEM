package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.RichTextEditor;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.citytechinc.cq.component.annotations.widgets.rte.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Krupa on 04/07/17.
 */

@Component(name = "qna", value = "Q&A Component",
        description = "Component for add question and answer of FAQ component",
        tabs = {
                @Tab( touchUINodeName = "tab1" , title = "QNA" )},
        actions = { "text: Q&A Component", "-", "editannotate", "copymove", "delete" },
        resourceSuperType = "foundation/components/parbase",
        group = " GWS-Global", path = "content")

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class QuestionAnswer {

    @DialogField(fieldLabel = "Question", required = true,
            additionalProperties = {@Property(name = "validateOnBlur", value = "true")})
    @TextField
    @Inject @Default(values = "")
    private String question;

    @DialogField(fieldLabel = "Answer", name = "./answer", required = true)
    @RichTextEditor(
            edit = @Edit,
            subsuperscript = @SubSuperscript,
            lists = @Lists,
            justify = @Justify,
            misctools = @MiscTools(specialchars = false, sourceedit = true),
            spellcheck = @SpellCheck,
            findreplace = @FindReplace,
            paraformat = {
                    @ParaFormat(tag = "h1", description = "Heading 1"),
                    @ParaFormat(tag = "h2", description = "Heading 2"),
                    @ParaFormat(tag = "h3", description = "Heading 3"),
                    @ParaFormat(tag = "h4", description = "Heading 4"),
                    @ParaFormat(tag = "h5", description = "Heading 5"),
                    @ParaFormat(tag = "p", description = "Paragraph")
            })
    @Inject @Named("answer")
    private String answerText;

    private String answer;

    @PostConstruct
    public void init(){
        if(null!=answerText) {
            this.answer = answerText;
        }
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

}
