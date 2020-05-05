package com.question.definition.definition_question;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.google.gson.annotations.SerializedName;
import com.question.Question;
import com.question.definition.definition_answer.DefinitionAnswer;
import io.swagger.annotations.ApiModelProperty;

@Entity
public class DefinitionQuestion extends Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("definitionQuestionId")
    @NotNull
    @ApiModelProperty(notes = "The definition question ID. It is unique",  required = true)
    private long id;

    @ApiModelProperty(notes = "List of users answers to the question")
    @OneToMany(cascade = CascadeType.ALL)
    private List<DefinitionAnswer> definitionAnswers;

    public DefinitionQuestion() {
        super();
        this.definitionAnswers = new ArrayList<>();
    }

    public DefinitionQuestion(String questionText) {
        super(questionText);
        this.definitionAnswers = new ArrayList<>();
    }

    public void update(DefinitionQuestion q) {
        if (q.getQuestionText() != null) {
            this.questionText = q.getQuestionText();
        }
    }

    public void addAnswer(DefinitionAnswer answer) {
        this.definitionAnswers.add(answer);
        this.totalAnswers += 1;
    }

    /**
     * Getters and Setters
     */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<DefinitionAnswer> getAnswers() {
        return definitionAnswers;
    }

    public void setAnswers(List<DefinitionAnswer> answers) {
        this.definitionAnswers = answers;
    }
}