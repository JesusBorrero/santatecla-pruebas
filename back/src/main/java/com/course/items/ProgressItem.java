package com.course.items;

import java.util.List;

public class ProgressItem {
    private String name;
    private List<Double> points;

    public ProgressItem(String name, List<Double> points){
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String studentName) {
        this.name = studentName;
    }

    public List<Double> getPoints() {
        return points;
    }

    public void setPoints(List<Double> points) {
        this.points = points;
    }
}
