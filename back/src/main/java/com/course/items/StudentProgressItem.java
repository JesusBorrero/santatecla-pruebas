package com.course.items;

import java.util.ArrayList;
import java.util.List;

public class StudentProgressItem {
    private String studentName;
    private List<Double> grades;
    private double average;

    public StudentProgressItem(String studentName){
        this.studentName = studentName;
        this.grades = new ArrayList<>();
        this.average = 0;
    }

    public void addGrade(Double m){
        this.grades.add(m);
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public List<Double> getGrades() {
        return grades;
    }

    public void setGrades(List<Double> modules) {
        this.grades = modules;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double totalAverage) {
        this.average = totalAverage;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof StudentProgressItem){
            StudentProgressItem item = (StudentProgressItem) obj;
            return item.getStudentName().equals(this.studentName);
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
