package com.slide;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
public interface SlideController {

    @ApiOperation(value = "Return all the slides")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All slides")
    })
    MappingJacksonValue slides();

    @ApiOperation(value = "Return the slide with the requested id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Slide"),
            @ApiResponse(code = 404, message = "Slide not found")
    })
    ResponseEntity<Slide> slide(@PathVariable long id);

    @ApiOperation(value = "Delete the slide with the requested id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted slide"),
            @ApiResponse(code = 404, message = "Slide not found")
    })
    ResponseEntity<Slide> deleteSlide(@PathVariable long id);

    @ApiOperation(value = "Update the slide with the requested id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated slide"),
            @ApiResponse(code = 404, message = "Slide not found")
    })
    ResponseEntity<Slide> updateSlide(@PathVariable long id, @RequestBody SlideDto slideDto);

    @ApiOperation(value = "Return the slide with the requested slideName, lessonName and unitName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Slide"),
            @ApiResponse(code = 404, message = "Slide not found")
    })
    ResponseEntity<List<Slide>> getSlideByName(@RequestParam String unitName, @RequestParam String lessonName, @RequestParam String slideName);

}
