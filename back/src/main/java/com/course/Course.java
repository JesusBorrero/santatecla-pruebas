package com.course;

import com.google.gson.annotations.SerializedName;
import com.itinerary.module.Module;
import com.user.User;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("courseId")
    @ApiModelProperty(notes = "The course ID. It is unique",  required = true)
    private long id;

    @ApiModelProperty(notes = "The course name", required = true)
    private String name;

    @ApiModelProperty(notes = "A short description of the course", required = true)
    private String description;

    @ApiModelProperty(notes = "The module that will be taught in the course", required = true)
    @ManyToOne
    private Module module;

    @ApiModelProperty(notes = "The students studying the course", required = true)
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<User> students;

    @ApiModelProperty(notes = "The teacher that created the course", required = true)
    @ManyToOne
    private User teacher;

    public Course(){
        this.students = new ArrayList<>();
    }

    public Course(String name, User teacher, String description){
        this.students = new ArrayList<>();
        this.name = name;
        this.teacher = teacher;
        this.description = description;
    }

    public void addStudent(User user){
        if(this.students.contains(user)){
            return;
        }
        this.students.add(user);
    }

    public void update(Course course){
        this.name = course.name;
        this.description = course.description;
        this.module = course.module;
        this.teacher = course.teacher;
    }

    public String toJson(){
        return "{\"name\":" + this.name + ",\n \"students\":[]}";
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Module getModule() {
        return module;
    }

    public List<User> getStudents() {
        return students;
    }

    public User getTeacher() {
        return teacher;
    }

    public String getDescription() {
        return description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
