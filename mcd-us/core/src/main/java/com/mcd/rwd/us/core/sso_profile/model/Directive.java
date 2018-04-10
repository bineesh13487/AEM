package com.mcd.rwd.us.core.sso_profile.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mcd.rwd.us.core.sso_profile.Serializable;

import java.util.LinkedList;
import java.util.List;

public class Directive implements Serializable {

    private List<DirectiveInput> inputs;

    public Directive() { }

    public Directive addInput(final DirectiveInput input) {
        if (inputs == null) {
            inputs = new LinkedList<>();
        }
        inputs.add(input);
        return this;
    }

    @JsonValue
    public List<DirectiveInput> getInputs() {
        return inputs;
    }

    public boolean includesRequiredFields() {
        if (inputs != null) {
            for (final DirectiveInput directiveInput : inputs) {
                if (directiveInput.isRequired()) {
                    return true;
                }
            }
        }
        return false;
    }
}
