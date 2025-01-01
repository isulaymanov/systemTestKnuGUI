package com.example.stestingknugui.model;

public class TestResult {
    private int id; // ID результата
    private String name; // Имя студента
    private String surname; // Фамилия студента
    private String dateCompletion; // Дата завершения теста
    private String result; // Результат теста
    private String startTime; // Время начала теста
    private String endTime; // Время окончания теста

    // Конструктор
    public TestResult(int id, String name, String surname, String dateCompletion, String result, String startTime, String endTime) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.dateCompletion = dateCompletion;
        this.result = result;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDateCompletion() {
        return dateCompletion;
    }

    public void setDateCompletion(String dateCompletion) {
        this.dateCompletion = dateCompletion;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
