package com.example.stestingknugui.model;

public class TestingGroup {
    private int id;
    private Group group;
    private Testing testing;

    public TestingGroup(int id, Group group, Testing testing) {
        this.id = id;
        this.group = group;
        this.testing = testing;
    }

    public int getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public Testing getTesting() {
        return testing;
    }
}
