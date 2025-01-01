package com.example.stestingknugui.model;

public class StudentQuestion {
    private int id;
    private String text;

    public StudentQuestion(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}