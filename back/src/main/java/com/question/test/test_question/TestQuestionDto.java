package com.question.test.test_question;

import com.question.QuestionDto;
import com.question.test.test_answer.TestAnswerDto;

import java.util.ArrayList;
import java.util.List;

public class TestQuestionDto extends QuestionDto {

    private long id;
    private List<String> possibleAnswers;
    private String correctAnswer;
    private List<TestAnswerDto> testAnswers;

    public TestQuestionDto() {
        super();
        this.possibleAnswers = new ArrayList<>();
        this.testAnswers = new ArrayList<>();
    }

    public TestQuestionDto(String questionText, List<String> possibleAnswers, String correctAnswer) {
        super(questionText);
        this.possibleAnswers = possibleAnswers;
        this.correctAnswer = correctAnswer;
        this.testAnswers = new ArrayList<>();
    }

    /**
     * Getters and Setters
     */

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<TestAnswerDto> getTestAnswers() {
        return testAnswers;
    }

    public void setPossibleAnswers(List<String> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setTestAnswers(List<TestAnswerDto> testAnswers) {
        this.testAnswers = testAnswers;
    }

}
