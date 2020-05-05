package com.itinerary.module;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

@Api
public interface UnitModuleController {
    @ApiOperation(value = "Create a new module into the unit with the requested unitId.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The module has ben successfully created into the unit"),
            @ApiResponse(code = 404, message = "Unit Not Found")
    })
    Module addModule(Module module, long unitId);

    @ApiOperation(value = "Delete the module with the requested moduleId from the unit with the requested unitId.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The module has ben successfully deleted from the unit"),
            @ApiResponse(code = 404, message = "Unit or Module Not Found")
    })
    ResponseEntity<Module> deleteModule(long moduleId, long unitId);
}
