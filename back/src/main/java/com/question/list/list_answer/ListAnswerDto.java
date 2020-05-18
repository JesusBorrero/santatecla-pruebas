package com.question.list.list_answer;

import com.question.AnswerDto;
import com.user.UserDto;

import java.util.ArrayList;
import java.util.List;

public class ListAnswerDto implements AnswerDto {

    protected long id;
    private List<String> answer;
    private boolean correct;
    private long unitId;
    private long blockId;
    private long courseId;
    private UserDto user;

    public ListAnswerDto() {
        this.answer = new ArrayList<>();
    }

    public ListAnswerDto(List<String> answer, boolean correct) {
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