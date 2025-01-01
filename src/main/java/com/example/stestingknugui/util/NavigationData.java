package com.example.stestingknugui.util;

import com.example.stestingknugui.model.student.Group;
import com.example.stestingknugui.model.student.Testing;


public class NavigationData {
    private static Group selectedGroup;
    private static Testing selectedTest;

    public static Group getSelectedGroup() {
        return selectedGroup;
    }

    public static void setSelectedGroup(Group group) {
        selectedGroup = group;
    }

    public static Testing getSelectedTest() {
        return selectedTest;
    }

    public static void setSelectedTest(Testing test) {
        selectedTest = test;
    }
}
