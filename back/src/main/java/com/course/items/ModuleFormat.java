package com.course.items;

import java.util.ArrayList;
import java.util.List;

public class ModuleFormat {
    private Long id;
    private String moduleName;
    private List<String> questions;

    public ModuleFormat(String name){
        this.moduleName = name;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(String q){
        this.questions.add(q);
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
