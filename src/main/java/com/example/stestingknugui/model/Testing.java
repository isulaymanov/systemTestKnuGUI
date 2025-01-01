package com.example.stestingknugui.model;

public class Testing {
    private int id; // ID теста
    private String name; // Название теста
    private String description; // Описание теста
    private int passDate; // Дата прохождения
    private int limitDate; // Дата окончания

    // Конструктор
    public Testing(int id, String name, String description, int passDate, int limitDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.passDate = passDate;
        this.limitDate = limitDate;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPassDate() {
        return passDate;
    }

    public void setPassDate(int passDate) {
        this.passDate = passDate;
    }

    public int getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(int limitDate) {
        this.limitDate = limitDate;
    }
}
