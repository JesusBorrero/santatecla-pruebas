package com.course.items;

public class ProgressInfo {
    private String name;
    private Double realization;
    private Double grade;

    public ProgressInfo(String name){
        this.name = name;
    }

    public String getNombre() {
        return name;
    }

    public void setNombre(String name) {
        this.name = name;
    }

    public Double getRealizacion() {
        return realization;
    }

    public void setRealizacion(Double realization) {
        this.realization = realization;
    }

    public Double getMedia() {
        return grade;
    }

    public void setMedia(Double grade) {
        if(Double.isNaN(grade)){
            this.grade = 0.0;
        }
        else {
            this.grade = grade;
        }
    }
}
