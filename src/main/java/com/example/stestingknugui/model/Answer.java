package com.example.stestingknugui.model;

import javafx.beans.property.*;

public class Answer {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty answerText = new SimpleStringProperty();
    private final BooleanProperty isCorrect = new SimpleBooleanProperty();

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getAnswerText() {
        return answerText.get();
    }

    public void setAnswerText(String answerText) {
        this.answerText.set(answerText);
    }

    public StringProperty answerTextProperty() {
        return answerText;
    }

    public boolean isIsCorrect() {
        return isCorrect.get();
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect.set(isCorrect);
    }

    public BooleanProperty isCorrectProperty() {
        return isCorrect;
    }
}
