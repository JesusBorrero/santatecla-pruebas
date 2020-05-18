package com.unit;

import com.card.CardDto;
import com.image.ImageDto;
import com.itinerary.lesson.LessonDto;
import com.itinerary.module.ModuleDto;
import com.question.definition.definition_question.DefinitionQuestionDto;
import com.question.list.list_question.ListQuestionDto;
import com.question.test.test_question.TestQuestionDto;
import com.relation.RelationDto;

import java.util.ArrayList;
import java.util.List;

public class UnitDto {

    protected long id;
    private String name;
    private List<CardDto> cards;
    private List<LessonDto> lessons;
    private List<ModuleDto> modules;
    private List<RelationDto> incomingRelations;
    private List<RelationDto> outgoingRelations;
    private List<DefinitionQuestionDto> definitionQuestions;
    private List<ListQuestionDto> listQuestions;
    private List<TestQuestionDto> testQuestions;
    private List<ImageDto> images;

    public UnitDto() {
        this.cards = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.modules = new ArrayList<>();
        this.incomingRelations = new ArrayList<>();
        this.outgoingRelations = new ArrayList<>();
        this.definitionQuestions = new ArrayList<>();
        this.listQuestions = new ArrayList<>();
        this.testQuestions = new ArrayList<>();
        this.images = new ArrayList<>();
    }

    public UnitDto(String name) {
        this();
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CardDto> getCards() {
        return cards;
    }

    public void setCards(List<CardDto> cards) {
        this.cards = cards;
    }

    public List<LessonDto> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonDto> lessons) {
        this.lessons = lessons;
    }

    public List<ModuleDto> getModules() {
        return modules;
    }

    public void setModules(List<ModuleDto> modules) {
        this.modules = modules;
    }

    public List<RelationDto> getIncomingRelations() {
        return incomingRelations;
    }

    public void setIncomingRelations(List<RelationDto> incomingRelations) {
        this.incomingRelations = incomingRelations;
    }

    public List<RelationDto> getOutgoingRelations() {
        return outgoingRelations;
    }

    public void setOutgoingRelations(List<RelationDto> outgoingRelations) {
        this.outgoingRelations = outgoingRelations;
    }

    public List<DefinitionQuestionDto> getDefinitionQuestions() {
        return definitionQuestions;
    }

    public void setDefinitionQuestions(List<DefinitionQuestionDto> definitionQuestions) {
        this.definitionQuestions = definitionQuestions;
    }

    public List<ListQuestionDto> getListQuestions() {
        return listQuestions;
    }

    public void setListQuestions(List<ListQuestionDto> listQuestions) {
        this.listQuestions = listQuestions;
    }

    public List<TestQuestionDto> getTestQuestions() {
        return testQuestions;
    }

    public void setTestQuestions(List<TestQuestionDto> testQuestions) {
        this.testQuestions = testQuestions;
    }

    public List<ImageDto> getImages() {
        return images;
    }

    public void setImages(List<ImageDto> images) {
        this.images = images;
    }
}