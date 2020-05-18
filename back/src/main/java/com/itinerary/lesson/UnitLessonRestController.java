package com.itinerary.lesson;

import java.util.Optional;
import java.util.stream.Collectors;

import com.GeneralRestController;
import com.slide.Slide;

import com.slide.SlideDto;
import com.unit.Unit;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/units")
public class UnitLessonRestController extends GeneralRestController implements UnitLessonController {

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/{unitId}/lessons/{lessonId}/slides/{slideId}")
    public ResponseEntity<Slide> getSlideFromLesson(@PathVariable long unitId, @PathVariable long lessonId, @PathVariable long slideId, HttpServletResponse response) {
        Optional<Unit> unit = unitService.findOne(unitId);

        if (unit.isPresent()) {
            Optional<Lesson> lesson = lessonService.findOne(lessonId);
            if (lesson.isPresent()) {
                Optional<Slide> slide = slideService.findOne(slideId);
                if (slide.isPresent()) {
                    return new ResponseEntity<>(slide.get(), HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{unitId}/lessons")
    public ResponseEntity<Lesson> addLesson(@RequestBody LessonDto lessonDto, @PathVariable long unitId) {
        Optional<Unit> unit = this.unitService.findOne(unitId);

        Lesson lesson = convertToEntity(lessonDto);

        if (unit.isPresent()) {
            this.lessonService.save(lesson);
            unit.get().getLessons().add(lesson);
            this.unitService.save(unit.get());

            return new ResponseEntity<>(lesson, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{unitId}/lessons/{lessonId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Lesson> deleteLesson(@PathVariable long lessonId, @PathVariable long unitId) {

        Optional<Unit> unit = this.unitService.findOne(unitId);

        if (unit.isPresent()) {
            Optional<Lesson> lesson = this.lessonService.findOne(lessonId);
            if (lesson.isPresent()) {
                unit.get().getLessons().remove(lesson.get());
                this.lessonService.delete(lessonId);
                return new ResponseEntity<>(lesson.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    private Lesson convertToEntity(LessonDto dto) {
        Lesson lesson = modelMapper.map(dto, Lesson.class);
        lesson.setSlides(dto.getSlides().stream()
                .map(this::convertToSlideEntity)
                .collect(Collectors.toList()));
        return lesson;
    }

    private Slide convertToSlideEntity(SlideDto dto) {
        return modelMapper.map(dto, Slide.class);
    }

}