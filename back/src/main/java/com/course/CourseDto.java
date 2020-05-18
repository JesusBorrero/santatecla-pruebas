package com.course;

import com.itinerary.module.ModuleDto;
import com.user.UserDto;

import java.util.ArrayList;
import java.util.List;

public class CourseDto {

    private long id;
    private String name;
    private String description;
    private ModuleDto module;
    private List<UserDto> students;
    private UserDto teacher;

    public CourseDto(){
        this.students = new ArrayList<>();
    }

    public CourseDto(String name, UserDto teacher, String description){
        this.students = new ArrayList<>();
        this.name = name;
        this.teacher = teacher;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public ModuleDto getModule() {
        return module;
    }

    public void setModule(ModuleDto module) {
        this.module = module;
    }

    public List<UserDto> getStudents() {
        return students;
    }

    public void setStudents(List<UserDto> students) {
        this.students = students;
    }

    public UserDto getTeacher() {
        return teacher;
    }

    public void setTeacher(UserDto teacher) {
        this.teacher = teacher;
    }
}
