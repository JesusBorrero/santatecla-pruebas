package com.itinerary.lesson;

import com.slide.Slide;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

@Api
public interface UnitLessonController {

    @ApiOperation(value = "Return the slide with the requested slideId that is into the lesson with the requested" +
            " lessonId and belongs to the unit with the requested unitId.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Slide"),
            @ApiResponse(code = 404, message = "Slide, Lesson or Unit Not Found")
    })
    ResponseEntity<Slide> getSlideFromLesson(long unitId, long lessonId, long slideId, HttpServletResponse response);

    @ApiOperation(value = "Create a new Lesson in the unit with the requested unitId.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The lesson has been successfully created."),
            @ApiResponse(code = 404, message = "Unit Not Found")
    })
    Lesson addLesson(Lesson lesson, long unitId);

    @ApiOperation(value = "Delete the lesson with the requested lessonId that belongs to the unit with the requested unitId.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The lesson has been successfully deleted."),
            @ApiResponse(code = 404, message = "Unit or Lesson Not Found")
    })
    ResponseEntity<Lesson> deleteLesson(long lessonId, long unitId);
}
