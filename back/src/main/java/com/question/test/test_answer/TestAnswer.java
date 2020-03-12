package com.question.test.test_answer;

import com.user.User;

import javax.persistence.*;

@Entity
public class TestAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    private String answerText;

    private boolean correct;

    private long unitId;
    private long blockId;
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

