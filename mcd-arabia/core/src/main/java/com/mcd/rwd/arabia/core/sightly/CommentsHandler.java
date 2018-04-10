package com.mcd.rwd.arabia.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.day.cq.wcm.api.Page;
import com.mcd.rwd.arabia.core.constants.ArabiaContactUsFormConstant;
import com.mcd.rwd.global.core.components.common.Text;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by deepti_b on 10-05-2017.
 */
@Component(name = "comments",value = "Arabia Comments",
        disableTargeting = true, group = "MCD ARABIA" , path="/content/arabiacontactus",
        actions = { "text: Arabia comment section", "-", "editannotate", "copymove", "delete", "insert" },
        listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
                @Listener(name = "afterinsert", value = "REFRESH_PAGE")},
        tabs = {
                @Tab( touchUINodeName = "section" , title = "Contact us comment Tab" )})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CommentsHandler {

    @DialogField(name = "./hideComments", fieldDescription = "This will hide Comments " +
            "completely from the page.",
            additionalProperties = {@Property(name = "inputValue", value = "true"),
                @Property(name = "value", value = "true")})
    @CheckBox(text = "Hide Comments ")
    @Inject @Named("hideComments")
    private boolean hideCommentsContent;

    @DialogField(fieldLabel = "Comment Text ",
            fieldDescription = "Heading text to be displayed for comment field. " +
                    "Default set to Enter your comments")
    @TextField
    @Inject @Default(values = ArabiaContactUsFormConstant.COMMENT_TEXT)
    private String commentMessage;

    @DialogField(name = "./commentReq", fieldLabel = "Comment Required",
            fieldDescription = "Please select comment is required or" +
            " not. Default set to Yes")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Yes" , value="yes"),
                    @Option(text="No" , value="no")
            })
    @Named("commentReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_YES_VALUE)
    private String commentValidation;

    @DialogField(name = "./charactersText", fieldLabel = "Character Text",
            fieldDescription = "Text to be displayed for comment validation. " +
                    "Default set to characters.")
    @TextField
    @Inject @Named("charactersText")
    @Default(values = ArabiaContactUsFormConstant.CHARACTERS_TEXT)
    private String characterText;

    @DialogField(name = "./commentCount",
            fieldLabel = "Character Count Value",
            fieldDescription = "Please provide maximum character count. Default set to '1000 " +
                    "for English and 500 for Arabic. Note: Count can have maximum " +
                    "of 1000 characters for English and 500 characters for Arabic")
    @TextField
    @Inject @Named("commentCount")
    private String CharactersCounter;

    @DialogField(fieldLabel = "Comment Validation Message",
            fieldDescription = "Validation message to be displayed for comment field. Default set " +
                    "to Please enter the 'Comments regarding your visit'")
    @TextField
    @Inject
    @Default(values = ArabiaContactUsFormConstant.COMMENT_VALIDATION_MESSAGE)
    private String CommentValidationMessage;

    @DialogField(name = "./optInText", fieldLabel = "Please provide text for Opt In Checkbox", fieldDescription = "Text for Opt In Checkbox." +
            " Default set to Please send me special offers, vouchers, news and/or information " +
            "on McDonald’s promotions & activities")
    @DialogFieldSet(title = "Please provide text for Opt In Checkbox" , namePrefix = "optInText/")
    @ChildResource(name = "optInText")
    @Inject @Named("optInText")
    Text optInTextData;

    @DialogField(name = "./legalText", fieldLabel = "Please provide text for legal Checkbox", fieldDescription = "Text for legal checkbox." +
            " Default set to Yes, I have read and agree to the McDonald’s Terms & Conditions and, " +
            "Privacy Policy")
    @DialogFieldSet(title = "Please provide text for legal Checkbox" , namePrefix = "legalText/")
    @ChildResource(name = "legalText")
    @Inject @Named("legalText")
    Text legalSectionText;

    @DialogField(fieldLabel = "Legal Validation Message",
            fieldDescription = "Validation message to be displayed for Terms & Condition field." +
                    " Default set to Please select Terms and Condition ")
    @TextField
    @Inject @Default(values = ArabiaContactUsFormConstant.LEGAL_VALIDATION_MESSAGE)
    private String legalValidationMessage;

    @DialogField(fieldLabel = "Submit Button Text",
            fieldDescription = "Text to be displayed for submit button. Default set to Submit")
    @TextField
    @Inject @Default(values = ArabiaContactUsFormConstant.SUBMIT_TEXT)
    private String submitButtonText;

    private static final Logger log = LoggerFactory.getLogger(CommentsHandler.class);

    private String commentCount = StringUtils.EMPTY;

    private String commentReq = StringUtils.EMPTY;

    private String commentValidationMessage = StringUtils.EMPTY;

    private String charactersText = StringUtils.EMPTY;

    private String legalText = "";

    private String optInText= StringUtils.EMPTY;

    private boolean hideComments = false;

    private StringBuffer formsFieldsBuffr=new StringBuffer();

    private String formFieldList="";

    @Inject
    Page currentPage;

    @Inject
    SlingScriptHelper slingScriptHelper;

    @PostConstruct
    public void activate() throws Exception {

        String lang = StringUtils.EMPTY;
        McdFactoryConfig mcdFactoryConfig;

        if(null!=currentPage) {
            String country = PageUtil.getCountry(currentPage);
            String language = PageUtil.getLanguage(currentPage);

            if (country != null && language != null) {
                McdFactoryConfigConsumer mcdFactoryConfigConsumer = slingScriptHelper.getService(McdFactoryConfigConsumer.class);
                if (mcdFactoryConfigConsumer != null) {
                    mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(country, language);
                    if (mcdFactoryConfig != null) {
                        lang = mcdFactoryConfig.getDnaLanguageCode();
                    }
                }
            }
        }

        if(lang.equalsIgnoreCase("ar")) {
            String count;
            try{
                count = ((Integer.parseInt(ArabiaContactUsFormConstant.COMMENT_COUNT))/2)+"";
                commentCount = StringUtils.isNotEmpty(CharactersCounter)? CharactersCounter : count;
            }catch(Exception e){
                commentCount = StringUtils.isNotEmpty(CharactersCounter)? CharactersCounter : ArabiaContactUsFormConstant.COMMENT_COUNT;
            }
        }
        else {
            commentCount = StringUtils.isNotEmpty(CharactersCounter)? CharactersCounter : ArabiaContactUsFormConstant.COMMENT_COUNT;
        }

        hideComments = this.hideCommentsContent;
        commentReq = this.commentValidation;
        populateFieldList(hideComments,ArabiaContactUsFormConstant.CONTACTUS_FEEDBACK);
        charactersText = this.characterText;
        if(null!=optInTextData) {
            optInText = null != optInTextData.getText() ? optInTextData.getText() : ArabiaContactUsFormConstant.OPTIN_TEXT;
        }else{
            optInText = ArabiaContactUsFormConstant.OPTIN_TEXT;
        }
        if(null!=legalSectionText) {
            legalText = null != legalSectionText.getText() ? legalSectionText.getText() : ArabiaContactUsFormConstant.LEGAL_TEXT;
        }else{
            legalText = ArabiaContactUsFormConstant.LEGAL_TEXT;
        }
        populateFieldList(false,ArabiaContactUsFormConstant.PERSONAL_INFO_OPTIN);
        populateFieldList(false,ArabiaContactUsFormConstant.PERSONAL_INFO_TERMSCONDITIONS);
        formFieldList=formsFieldsBuffr.toString();
    }

    private void tagRemoval(String oldHyperlink) {
        String[] hyper = oldHyperlink.split("");
        for (int counter = 0; counter < hyper.length; counter++) {
            if (counter > 2 && (hyper.length - (counter + 4)) > 1) {
                legalText = legalText + hyper[counter];
            }
        }
    }

    public void populateFieldList(Boolean displayFlag,String str){
        if(!displayFlag){
            formsFieldsBuffr.append(str);
            formsFieldsBuffr.append(',');
        }

    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public String getCommentReq() {
        return commentReq;
    }

    public String getCommentValidationMessage() {
        return commentValidationMessage;
    }

    public String getCharactersText() {
        return charactersText;
    }

    public String getLegalText() {
        return legalText;
    }

    public String getOptInText() {
        return optInText;
    }

    public String getSubmitButtonText() {
        return submitButtonText;
    }

    public boolean isHideComments() {
        return hideComments;
    }

    public String getLegalValidationMessage() {
        return legalValidationMessage;
    }
    public String getFormFieldList() {
        return formFieldList;
    }

}
