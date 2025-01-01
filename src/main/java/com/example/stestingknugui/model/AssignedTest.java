package com.example.stestingknugui.model;

import java.util.List;


public class AssignedTest {
    private String testName;
    private List<String> groupNames;

    // Конструктор
    public AssignedTest(String testName, List<String> groupNames) {
        this.testName = testName;
        this.groupNames = groupNames;
    }

    // Геттеры
    public String getTestName() {
        return testName;
    }

    public List<String> getGroupNames() {
        return groupNames;
    }

    // Метод toString для вывода в нужном формате
    @Override
    public String toString() {
        return testName + "(" + String.join(", ", groupNames) + ")";
    }
}
