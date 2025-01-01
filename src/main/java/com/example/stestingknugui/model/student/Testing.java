package com.example.stestingknugui.model.student;

public class Testing {
    private int id;
    private String name;
    private String description;
    private long passDate;
    private long limitDate;

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

    public long getPassDate() {
        return passDate;
    }

    public void setPassDate(long passDate) {
        this.passDate = passDate;
    }

    public long getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(long limitDate) {
        this.limitDate = limitDate;
    }

    @Override
    public String toString() {
        return name + " — " + description;
    }
}