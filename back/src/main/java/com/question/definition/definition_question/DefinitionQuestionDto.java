package com.question.definition.definition_question;

import com.question.definition.definition_answer.DefinitionAnswerDto;
import com.question.QuestionDto;

import java.util.ArrayList;
import java.util.List;

public class DefinitionQuestionDto extends QuestionDto {

    private long id;
    private List<DefinitionAnswerDto> definitionAnswers;

    public DefinitionQuestionDto() {
        super();
        this.definitionAnswers = new ArrayList<>();
    }

    public DefinitionQuestionDto(String questionText) {
        super(questionText);
        this.definitionAnswers = new ArrayList<>();
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

    public List<DefinitionAnswerDto> getAnswers() {
        return definitionAnswers;
    }

    public void setAnswers(List<DefinitionAnswerDto> answers) {
        this.definitionAnswers = answers;
    }
}