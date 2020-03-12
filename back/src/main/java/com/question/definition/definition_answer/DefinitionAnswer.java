package com.question.definition.definition_answer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.user.User;

@Entity
public class DefinitionAnswer{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    private String answerText;

    //Correct or wrong
    private boolean correct;

    //Corrected by the teacher
    private boolean corrected;

    private String justification;

    private long unitId;
    private long blockId;
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