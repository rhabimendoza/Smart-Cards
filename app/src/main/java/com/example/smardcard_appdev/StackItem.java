package com.example.smardcard_appdev;

public class StackItem {

    long stackId;
    String stackName;
    String stackDesc;

    public StackItem(long stackId, String stackName, String stackDesc) {
        this.stackId = stackId;
        this.stackName = stackName;
        this.stackDesc = stackDesc;
    }

    public long getstackId() {
        return stackId;
    }

    public String getStackName() {
        return stackName;
    }

    public String getStackDesc() {
        return stackDesc;
    }
}