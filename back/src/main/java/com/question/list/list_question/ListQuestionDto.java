package com.question.list.list_question;

import com.question.QuestionDto;
import com.question.list.list_answer.ListAnswerDto;

import java.util.ArrayList;
import java.util.List;


public class ListQuestionDto extends QuestionDto {

    private long id;
    private List<String> possibleAnswers;
    private List<String> correctAnswers;
    private List<ListAnswerDto> listAnswers;

    public ListQuestionDto() {
        super();
        this.possibleAnswers = new ArrayList<>();
        this.correctAnswers = new ArrayList<>();
        this.listAnswers = new ArrayList<>();
    }

    public ListQuestionDto(String questionText, List<String> possibleAnswers, List<String> correctAnswer) {
        super(questionText);
        this.possibleAnswers = possibleAnswers;
        this.correctAnswers = correctAnswer;
        this.listAnswers = new ArrayList<>();
    }

    public void addAnswer(ListAnswerDto answer) {
        this.listAnswers.add(answer);
    }

    /**
     * Getters and Setters
     */

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(List<String> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public List<ListAnswerDto> getListAnswers() {
        return listAnswers;
    }

    public void setListAnswers(List<ListAnswerDto> listAnswers) {
        this.listAnswers = listAnswers;
    }
}