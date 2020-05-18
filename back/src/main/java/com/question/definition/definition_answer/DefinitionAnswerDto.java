package com.question.definition.definition_answer;

import com.question.AnswerDto;
import com.user.UserDto;

public class DefinitionAnswerDto implements AnswerDto {

    protected long id;
    private String answerText;
    private boolean correct;
    private boolean corrected;
    private String justification;
    private long unitId;
    private long blockId;
    private long courseId;
    private UserDto user;

    public DefinitionAnswerDto(){}

    public DefinitionAnswerDto(String answerText){
        this.answerText = answerText;
        this.correct = false;
        this.corrected = false;
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

    public boolean isCorrected() {
        return corrected;
    }

    public void setCorrected(boolean corrected) {
        this.corrected = corrected;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
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