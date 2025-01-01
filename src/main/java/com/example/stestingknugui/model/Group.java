package com.example.stestingknugui.model;

public class Group {
    private final Integer id;
    private String name;

    public Group(int id, String name) {
        this.id = id;
        this.name = name;
    }


    // Добавьте геттеры, если нужно
    public Integer getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }


}
