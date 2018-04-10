package com.mcd.rwd.us.core.sso_profile.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.icfolson.aem.library.api.node.ComponentNode;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class DirectiveInput {

    private String name;
    private String label;
    private String placeholder;
    private String type;
    private boolean required;
    private String validation;
    private String errorMsg;
    private String requireMsg;
    private String description;
    private String note;
    private String value;
    private Boolean checkedByDefault;
    private List<Option> options;


    public DirectiveInput(final String name, final String label, final String type) {
        this.name = name;
        this.label = label;
        this.type = type;
    }

    public DirectiveInput(final ComponentNode componentNode) {
        name = componentNode.getResource().getName();
        label = componentNode.get("label", "");
        placeholder = componentNode.get("placeholder", "");
        required = componentNode.get("required", false);
        validation = componentNode.get("validationRegEx", "");
        errorMsg = componentNode.get("validationRegExError", SSOProfileConstants.FIELD_NOT_VALID_DEFAULT_ERROR);
        requireMsg = componentNode.get("requiredError", SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR);
        description = componentNode.get("description", "");
        note = componentNode.get("note", "");
        value = componentNode.get("value", "");
        if (componentNode.get("checkedByDefault", Boolean.class).isPresent()) {
            checkedByDefault = componentNode.get("checkedByDefault", Boolean.class).get();
        }
        //might need more code (could check for options too)
    }

    public DirectiveInput setName(final String name) {
        this.name = name;
        return this;
    }

    public DirectiveInput setLabel(final String label) {
        this.label = label;
        return this;
    }

    public DirectiveInput setType(final String type) {
        this.type = type;
        return this;
    }

    public DirectiveInput setRequired() {
        required = true;
        return this;
    }

    public DirectiveInput setRequired(final boolean required) {
        this.required = required;
        return this;
    }

    public DirectiveInput addPlaceholder(final String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public DirectiveInput addValidation(final String validation) {
        this.validation = validation;
        return this;
    }

    public DirectiveInput addErrorMsg(final String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public DirectiveInput addRequireMsg(final String requireMsg) {
        this.requireMsg = requireMsg;
        return this;
    }

    public DirectiveInput addDescription(final String description) {
        this.description = description;
        return this;
    }

    public DirectiveInput addNote(final String note) {
        this.note = note;
        return this;
    }

    public DirectiveInput addValue(final String value) {
        this.value = value;
        return this;
    }

    public DirectiveInput addOption(final String optionLabel, final String optionValue) {
        if (options == null) {
            options = new LinkedList<>();
        }
        options.add(new Option(optionLabel, optionValue));
        return this;
    }

    public DirectiveInput addOptions(final List<String> optionsList) {
        if (options == null) {
            options = new LinkedList<>();
        }
        for (final String optionComposite : optionsList) {
            options.add(new Option(StringUtils.substringAfter(optionComposite, "::"), StringUtils.substringBefore(optionComposite, "::")));
        }
        return this;
    }

    @JsonProperty
    public String getName() {
        return name;
    }
    @JsonProperty
    public String getLabel() {
        return label;
    }
    @JsonProperty
    public String getPlaceholder() {
        return placeholder;
    }
    @JsonProperty
    public boolean isRequired() {
        return required;
    }
    @JsonProperty
    public String getType() {
        return type;
    }
    @JsonProperty
    public String getValidation() {
        return validation;
    }
    @JsonProperty
    public String getErrorMsg() {
        return errorMsg;
    }
    @JsonProperty
    public String getRequireMsg() {
        return requireMsg;
    }
    @JsonProperty
    public String getDescription() {
        return description;
    }
    @JsonProperty
    public String getNote() {
        return note;
    }
    @JsonProperty
    public Boolean isCheckedByDefault() {
        return checkedByDefault;
    }
    @JsonProperty
    public String getValue() {
        return value;
    }
    @JsonProperty
    public List<Option> getOptions() {
        return options;
    }


    private class Option {

        private String label;
        private String value;

        Option(final String label, final String value) {
            this.label = label;
            this.value = value;
        }
        @JsonProperty
        public String getLabel() {
            return label;
        }
        @JsonProperty
        public String getValue() {
            return value;
        }
    }
}


