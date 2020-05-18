package com.question.test.test_answer;

import com.question.AnswerDto;
import com.user.UserDto;

public class TestAnswerDto implements AnswerDto {

    protected long id;
    private String answerText;
    private boolean correct;
    private long unitId;
    private long blockId;
    private long courseId;
    private UserDto user;

    public TestAnswerDto() {
    }

    public TestAnswerDto(String answerText, boolean correct) {
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

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
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

