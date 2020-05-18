package com.question.definition.definition_answer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.question.Answer;
import com.user.User;
import io.swagger.annotations.ApiModelProperty;

@Entity
public class DefinitionAnswer implements Answer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The answer ID. It is unique",  required = true)
    protected long id;

    @ApiModelProperty(notes = "The answer itself. It's an open response",  required = true)
    private String answerText;

    // Correct or wrong
    @ApiModelProperty(notes = "It indicates if the answer is right or wrong")
    private boolean correct;

    // Corrected by the teacher
    @ApiModelProperty(notes = "It indicates if the answer is corrected by the teacher")
    private boolean corrected;

    @ApiModelProperty(notes = "Teacher justification")
    private String justification;

    @ApiModelProperty(notes = "Unit to which the question belongs")
    private long unitId;
    @ApiModelProperty(notes = "Block to which the question belongs")
    private long blockId;
    @ApiModelProperty(notes = "Course to which the question belongs")
    private long courseId;

    @OneToOne
    private User user;

    public DefinitionAnswer(){}

    public DefinitionAnswer(String answerText){
        this.answerText = answerText;
        this.correct = false;
        this.corrected = false;
    }

    public void update(DefinitionAnswer a) {
        if (getAnswerText() != null) {
            this.answerText = a.getAnswerText();
        }
        if (a.getJustification() != null) {
            this.justification = a.getJustification();
        }

        this.correct = a.isCorrect();
        this.corrected = a.isCorrected();
        this.unitId = a.unitId;
        this.blockId = a.blockId;
        this.courseId = a.courseId;
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