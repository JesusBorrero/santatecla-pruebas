package com.unit;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.card.Card;
import com.image.Image;
import com.itinerary.module.Module;
import com.question.definition.definition_question.DefinitionQuestion;
import com.itinerary.lesson.Lesson;
import com.question.list.list_question.ListQuestion;
import com.question.test.test_question.TestQuestion;
import com.relation.Relation;

@Entity
public class Unit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    private String name;
    
    @OneToMany
    @OrderColumn
    private List<Card> cards;

    @OneToMany
    @OrderColumn
    private List<Lesson> lessons;

    @OneToMany
    @OrderColumn
    private List<Module> modules;

    @OneToMany
    private List<Relation> incomingRelations;

    @OneToMany
    @OrderColumn
    private List<Relation> outgoingRelations;

    @ManyToMany
    private List<DefinitionQuestion> definitionQuestions;

    @ManyToMany
    private List<ListQuestion> listQuestions;

    @ManyToMany
    private List<TestQuestion> testQuestions;

    @OneToMany
    private List<Image> images;

    public Unit() {
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

    public Unit(String name) {
        this();
        this.name = name;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Iterable<Card> getCards() {
        return this.cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Card getCard(long id) {
        for (Card card: this.cards){
            if (card.getId() == id){
                return card;
            }
        }
        return null;
    }

    public void deleteCard(long id) {
        Card card = getCard(id);
        if (card != null) {
            this.cards.remove(card);
        }
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public List<Lesson> getLessons() {
        return this.lessons;
    }

    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public List<Relation> getIncomingRelations() {
        return this.incomingRelations;
    }

    public List<Relation> getOutgoingRelations() {
        return this.outgoingRelations;
    }

    private Relation getRelation(List<Relation> relations, long id) {
        for (Relation relation : relations) {
            if (relation.getId() == id) {
                return relation;
            }
        }
        return null;
    }

    public void addIncomingRelation(Relation incomingRelation) {
        if (this.incomingRelations.contains(incomingRelation)) {
            this.getRelation(this.incomingRelations, incomingRelation.getId()).update(incomingRelation);
        } else {
            int index = 0;
            for (Relation relation : this.incomingRelations) {
                if (Relation.compareType(incomingRelation.getRelationType(), relation.getRelationType()) <= 0) {
                    index++;
                    break;
                }
            }
            ArrayList<Relation> sublist = new ArrayList<>(this.incomingRelations.subList(index, this.incomingRelations.size()));
            this.incomingRelations.removeAll(sublist);
            this.incomingRelations.add(incomingRelation);
            this.incomingRelations.addAll(sublist);
        }
    }

    public void addOutgoingRelation(Relation outgoingRelation) {
        if (this.outgoingRelations.contains(outgoingRelation)) {
            this.getRelation(this.outgoingRelations, outgoingRelation.getId()).update(outgoingRelation);
        } else {
            int index = 0;
            for (Relation relation : this.outgoingRelations) {
                if (Relation.compareType(outgoingRelation.getRelationType(), relation.getRelationType()) <= 0) {
                    index++;
                    break;
                }
            }
            ArrayList<Relation> sublist = new ArrayList<>(this.outgoingRelations.subList(index, this.outgoingRelations.size()));
            this.outgoingRelations.removeAll(sublist);
            this.outgoingRelations.add(outgoingRelation);
            this.outgoingRelations.addAll(sublist);
        }
    }

    public List<DefinitionQuestion> getDefinitionQuestions() {
        return this.definitionQuestions;
    }

    public void addDefinitionQuestion(DefinitionQuestion definitionQuestion) {
        this.definitionQuestions.add(definitionQuestion);
    }
    public void addListQuestion(ListQuestion listQuestion) {
        this.listQuestions.add(listQuestion);
    }
    public void addTestQuestion(TestQuestion testQuestion) {
        this.testQuestions.add(testQuestion);
    }

    public void setDefinitionQuestions(List<DefinitionQuestion> definitionQuestions){
        this.definitionQuestions = definitionQuestions;
    }

    public List<ListQuestion> getListQuestions() {
        return this.listQuestions;
    }

    public void setListQuestions(List<ListQuestion> listQuestions) {
        this.listQuestions = listQuestions;
    }


    public List<TestQuestion> getTestQuestions() {
        return testQuestions;
    }

    public void setTestQuestions(List<TestQuestion> testQuestions) {
        this.testQuestions = testQuestions;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void addImage(Image image){
        this.images.add(image);
    }
}