package com.question.test.test_question;

import com.google.gson.annotations.SerializedName;
import com.question.Question;
import com.question.test.test_answer.TestAnswer;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TestQuestion extends Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("testQuestionId")
    @NotNull
    @ApiModelProperty(notes = "The test question ID. It is unique",  required = true)
    private long id;

    @ApiModelProperty(notes = "List of possible answers to the question")
    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<String> possibleAnswers;

    @ApiModelProperty(notes = "The unique correct answer", required = true)
    private String correctAnswer;

    @ApiModelProperty(notes = "List of users answers to the question")
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
