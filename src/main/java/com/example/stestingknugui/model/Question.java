package com.example.stestingknugui.model;

import javafx.beans.property.*;

public class Question {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty questionText = new SimpleStringProperty();

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getQuestionText() {
        return questionText.get();
    }

    public void setQuestionText(String questionText) {
        this.questionText.set(questionText);
    }

    public StringProperty questionTextProperty() {
        return questionText;
    }
}
