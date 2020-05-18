package com.itinerary.lesson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;

@Api
public interface LessonController {
    @ApiOperation(value = "Return all the lessons.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lessons")
    })
    MappingJacksonValue lessons();

    @ApiOperation(value = "Return the lesson with the requested id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lesson"),
            @ApiResponse(code = 404, message = "Lesson Not Found")
    })
    ResponseEntity<Lesson> lesson(long lessonId);

    @ApiOperation(value = "Delete the lesson with the requested id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The lesson has ben successfully deleted"),
            @ApiResponse(code = 404, message = "Lesson Not Found")
    })
    ResponseEntity<Lesson> deleteLesson(long lessonId);

    @ApiOperation(value = "Update the lesson with the requested id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The lesson has ben successfully updated"),
            @ApiResponse(code = 404, message = "Lesson Not Found")
    })
    ResponseEntity<Lesson> updateLesson(long lessonId, LessonDto lessonDto);
}
