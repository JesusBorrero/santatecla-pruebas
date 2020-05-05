package com.itinerary.lesson;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.itinerary.block.Block;
import com.slide.*;
import io.swagger.annotations.ApiModelProperty;

@Entity
public class Lesson extends Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The lesson name", required = true)
    protected long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderColumn
    @ApiModelProperty(notes = "A list with the slides inside the lesson", required = true)
    private List<Slide> slides;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    @ApiModelProperty(notes = "A list with the ids of the questions inside the lesson", required = true)
    private List<Long> questionsIds;

    public Lesson(){
        this.slides = new ArrayList<>();
    }

    public Lesson(String name){
        super(name);
        this.slides = new ArrayList<>();
        this.questionsIds = new ArrayList<>();
    }

    public void update(Lesson lesson) {
        this.name = lesson.getName();
        this.slides = lesson.getSlides();
        this.questionsIds = lesson.getQuestionsIds();
    }

    public List<Long> compareId(List<Slide> slides) {
        List<Long> diferences = new ArrayList<>();
        List<Long> slidesId = new ArrayList<>();
        List<Long> inSlidesId = new ArrayList<>();
        for (Slide slide: this.getSlides()) {
            slidesId.add(slide.getId());
        }
        for (Slide slide: slides) {
            inSlidesId.add(slide.getId());
        }
        for (long id: slidesId) {
            if (!(inSlidesId.contains(id))) {
                diferences.add(id);
            }
        }
        return diferences;
    }


    /********************
     * GETTER AND SETTER *
     ********************/

    public long getId() { return id; }

    public String getName() { return name; }

    public List<Long> getQuestionsIds() {
        return questionsIds;
    }

    public List<Slide> getSlides() { return slides; }

    public void setId(long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setQuestionsIds(List<Long> questionsIds) {
        this.questionsIds = questionsIds;
    }

    public void setSlides(List<Slide> slides) { this.slides = slides; }

}