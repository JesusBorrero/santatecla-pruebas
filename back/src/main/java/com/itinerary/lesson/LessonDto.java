package com.itinerary.lesson;

import com.itinerary.block.BlockDto;
import com.slide.SlideDto;

import java.util.ArrayList;
import java.util.List;

public class LessonDto extends BlockDto {

    protected long id;
    private List<SlideDto> slides;
    private List<Long> questionsIds;

    public LessonDto(){
        this.slides = new ArrayList<>();
    }

    public LessonDto(String name){
        super(name);
        this.slides = new ArrayList<>();
        this.questionsIds = new ArrayList<>();
    }

    /********************
     * GETTER AND SETTER *
     ********************/

    @Override
    public long getId() { return id; }

    @Override
    public String getName() { return name; }

    public List<Long> getQuestionsIds() {
        return questionsIds;
    }

    public List<SlideDto> getSlides() { return slides; }

    @Override
    public void setId(long id) { this.id = id; }

    @Override
    public void setName(String name) { this.name = name; }

    public void setQuestionsIds(List<Long> questionsIds) {
        this.questionsIds = questionsIds;
    }

    public void setSlides(List<SlideDto> slides) { this.slides = slides; }

}