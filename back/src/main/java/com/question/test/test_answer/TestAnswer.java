package com.question.test.test_answer;

import com.question.Answer;
import com.user.User;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class TestAnswer implements Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @ApiModelProperty(notes = "The answer ID. It is unique.",  required = true)
    protected long id;

    @ApiModelProperty(notes = "The answer itself. It's the select answer from possible answers",  required = true)
    private String answerText;

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

    public TestAnswer() {
    }

    public TestAnswer(String answerText, boolean correct) {
        this.answerText = answerText;
        this.correct = correct;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
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

