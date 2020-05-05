package com.slide;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SlideRepository extends JpaRepository<Slide, Long> {

    @Query(value = "select slide.id, slide.name, slide.content from slide left join block_slides on slide.id = block_slides.slides_id left join block on block.id = block_slides.lesson_id left join unit_lessons on block.id = unit_lessons.lessons_id left join unit on unit.id = unit_lessons.unit_id where unit.name = ?1 and block.name = ?2 and slide.name = ?3", nativeQuery = true)
    public List<Slide> findByName(String unitName, String lessonName, String slideName);

    public List<Slide> findByContentContaining(String oldName);
    public List<Slide> findByContentContainingAndContentContaining(String insert, String oldName);
    public List<Slide> findByContentContainingOrContentContaining(String insertCard, String insertSlide);
}