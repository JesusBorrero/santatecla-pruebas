package com.itinerary.lesson;

import java.util.List;
import java.util.Optional;

import com.GeneralRestController;
import com.slide.Slide;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lessons")
public class LessonRestController extends GeneralRestController implements LessonController{

    @GetMapping(value="/")
    public MappingJacksonValue lessons(){
        MappingJacksonValue result = new MappingJacksonValue(this.lessonService.findAll());
        return result;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Lesson> lesson(@PathVariable long id){
        Optional<Lesson> lesson = this.lessonService.findOne(id);
        if (lesson.isPresent()) {
            return new ResponseEntity<>(lesson.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Lesson> deleteLesson(@PathVariable long id){
        Optional<Lesson> lesson = this.lessonService.findOne(id);
        if(lesson.isPresent()){
            this.lessonService.delete(id);
            return new ResponseEntity<>(lesson.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable long id, @RequestBody Lesson lesson) {
        Optional<Lesson> l = this.lessonService.findOne(id);
        Slide oldSlide;
        if (l.isPresent()) {
            if(!lesson.getName().equals(l.get().getName())){
                long unitId = this.unitService.findLessonUnit(l.get().getId());
                this.slideService.updateAllSlidesLessonName(l.get().getName(), lesson.getName(),
                        this.unitService.findOne(unitId).get().getName(), lesson);
            }
            for (Slide slide: lesson.getSlides()) {
                oldSlide = this.slideService.findOne(slide.getId()).get();
                if(!oldSlide.getName().equals(slide.getName())){
                    this.slideService.updateAllSlidesSlideName(l.get().getName(), oldSlide.getName(), slide.getName(), lesson);
                }
                this.slideService.save(slide);
            }

            List<Long> diferences = l.get().compareId(lesson.getSlides());
            for (long diferenceId: diferences) {
                this.slideService.delete(diferenceId);
            }
        }
        this.lessonService.save(lesson);
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }

}