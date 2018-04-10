package com.mcd.yrtk.components.content.questionfeed.model;

import java.util.List;

public final class Result {
    private List<QuestionDetails> rows;

    public List<QuestionDetails> getRows() {
        return rows;
    }

    public void setRows(final List<QuestionDetails> rows) {
        this.rows = rows;
    }
}

