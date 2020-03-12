package com.question.test.test_question;

import com.google.gson.annotations.SerializedName;
import com.question.Question;
import com.question.test.test_answer.TestAnswer;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TestQuestion extends Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("testQuestionId")
    private long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<String> possibleAnswers;

    private String correctAnswer;

    @OneToMany(cascade = CascadeType.ALL)
    private List<TestAnswer> testAnswers;

    public TestQuestion() {
        super();
        this.possibleAnswers = new ArrayList<>();
        this.testAnswers = new ArrayList<>();
    }

    public TestQuestion(String questionText, List<String> possibleAnswers, String correctAnswer) {
        super(questionText);
        this.possibleAnswers = possibleAnswers;
        this.correctAnswer = correctAnswer;
        this.testAnswers = new ArrayList<>();
    }

    public void update(TestQuestion q) {
        if (q.getQuestionText() != null) {
            this.questionText = q.getQuestionText();
        }
        if (!q.getCorrectAnswer().equals(this.correctAnswer)) {
            this.correctAnswer = q.getCorrectAnswer();
        }
        if (!q.getPossibleAnswers().equals(this.possibleAnswers)) {
            this.possibleAnswers = q.getPossibleAnswers();
        }
    }

    public void addAnswer(TestAnswer answer) {
        this.testAnswers.add(answer);
    }

    /**
     * Getters and Setters
     */

    public long getId() {
        return id;
    }

    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<TestAnswer> getTestAnswers() {
        return testAnswers;
    }

}
