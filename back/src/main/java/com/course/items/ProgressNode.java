package com.course.items;

import java.util.ArrayList;
import java.util.List;

public class ProgressNode {
    private ProgressInfo value;
    private List<ProgressNode> children;

    public ProgressNode(String name){
        this.value = new ProgressInfo(name);
        this.children = new ArrayList<>();
    }

    public void setChildren(List<ProgressNode> children) {
        this.children = children;
    }

    public void addChild(ProgressNode item){
        this.children.add(item);
    }

    public List<ProgressNode> getChildren() {
        return children;
    }

    public ProgressInfo getValue() {
        return value;
    }

    public void setValue(ProgressInfo value) {
        this.value = value;
    }
}
