package com.question.list.list_answer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.question.Answer;
import com.user.User;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ListAnswer implements Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @ApiModelProperty(notes = "The answer ID. It is unique",  required = true)
    protected long id;

    @ApiModelProperty(notes = "The answer itself. It's a list of selected possible answers",  required = true)
    @ElementCollection
    private List<String> answer;

    @ApiModelProperty(notes = "It indicates if the answer is right or wrong")
    private boolean correct;

    @ApiModelProperty(notes = "Unit to which the question belongs")
    private long unitId;
    @ApiModelProperty(notes = "Block to which the question belongs")
    private long blockId;
    @ApiModelProperty(notes = "Course to which the question belongs")
    private long courseId;

    @OneToOne
    private User user;

    public ListAnswer() {
        this.answer = new ArrayList<>();
    }

    public ListAnswer(ArrayList<String> answer, boolean correct) {
        this.answer = answer;
        this.correct = correct;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getUnitId() {
        return unitId;
    }

    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }

    public long getBlockId() {
        return blockId;
    }

    public void setBlockId(long blockId) {
        this.blockId = blockId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}