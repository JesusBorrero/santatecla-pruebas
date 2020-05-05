package com.unit;

import com.image.Image;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Api
public interface UnitController {

    @ApiOperation(value = "Return all the units")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All units")
    })
    ResponseEntity<List<Unit>> getUnits();

    @ApiOperation(value = "Return the unit with the requested id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Unit"),
            @ApiResponse(code = 404, message = "Unit not found")
    })
    ResponseEntity<Unit> getUnit(@PathVariable int id);

    @ApiOperation(value = "Create a new unit")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Created unit"),
            @ApiResponse(code = 409, message = "Conflict trying to create the unit")
    })
    ResponseEntity<Unit> createUnit(@RequestBody Unit unit);

    @ApiOperation(value = "Update all cards of the requested unit")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Unit updated"),
            @ApiResponse(code = 404, message = "Unit not found")
    })
    ResponseEntity<Unit> updateUnitCards(@PathVariable int id, @RequestBody Unit unit);

    @ApiOperation(value = "Update a all of the requested units and its relations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of updated units"),
            @ApiResponse(code = 404, message = "Unit not found"),
            @ApiResponse(code = 409, message = "Conflict trying to update a unit or relation")
    })
    ResponseEntity<List<Unit>> updateUnits(@RequestBody List<Unit> units);

    @ApiOperation(value = "Delete the unit with the requested id and all its relations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Unit has been deleted"),
            @ApiResponse(code = 404, message = "Unit not found"),
            @ApiResponse(code = 409, message = "Conflict trying to delete a unit or relation")
    })
    ResponseEntity<Unit> deleteUnit(@PathVariable long id);

    @ApiOperation(value = "Search units that contains the requested name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of units that matches")
    })
    ResponseEntity<List<Unit>> searchUnits(@RequestParam String name);

    @ApiOperation(value = "Return the unambiguous name of the unit with the requested id, that is the shortest name that makes it possible to identify that unit only by its name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Dummy unit with unambiguous name"),
            @ApiResponse(code = 404, message = "Unit not found")
    })
    ResponseEntity<Unit> getUnitUnambiguousName(@PathVariable int id);

    @ApiOperation(value = "Return the absolute name of the unit with the requested id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Dummy unit with absolute name"),
            @ApiResponse(code = 404, message = "Unit not found")
    })
    ResponseEntity<Unit> getUnitAbsoluteName(@PathVariable int id);

    @ApiOperation(value = "Return the parent of the unit with the requested id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Parent unit"),
            @ApiResponse(code = 404, message = "Unit not found")
    })
    ResponseEntity<Unit> getUnitParent(@PathVariable int id);

    @ApiOperation(value = "Return if the name of the requested unit is valid, this means does not conflict")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Is valid name")
    })
    ResponseEntity<Boolean> validName(@RequestBody Unit unit);

    @ApiOperation(value = "Return the name of the unit with the requested id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Unit name"),
            @ApiResponse(code = 404, message = "Unit not found")
    })
    ResponseEntity<Object> getUnitName(@PathVariable int id);

    @ApiOperation(value = "Return all the images of the unit with the requested id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All images"),
            @ApiResponse(code = 404, message = "Unit not found")
    })
    ResponseEntity<MappingJacksonValue> getUnitImages(@PathVariable int id);

    @ApiOperation(value = "Return the image with the requested imageId of the unit with the requested unitId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Images"),
            @ApiResponse(code = 404, message = "Unit or image not found")
    })
    ResponseEntity<Image> getImage(@PathVariable int unitId, @PathVariable int imageId);

    @ApiOperation(value = "Add a new image to the unit with the requested unitId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Added image"),
            @ApiResponse(code = 404, message = "Unit not found")
    })
    ResponseEntity<Image> addImage(@RequestParam(value = "imageFile") MultipartFile imageFile, @PathVariable int unitId);

    @ApiOperation(value = "Delete the image with the requested imageId of the unit with the requested unitId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Image has been deleted"),
            @ApiResponse(code = 404, message = "Unit or image not found")
    })
    ResponseEntity<Image> deleteImage(@PathVariable int unitId, @PathVariable int imageId);

    @ApiOperation(value = "Return the module with the requested id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Module"),
            @ApiResponse(code = 404, message = "Module not found")
    })
    ResponseEntity<Unit> getModuleUnit(@PathVariable long id);

}
