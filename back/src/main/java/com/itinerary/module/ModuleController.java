package com.itinerary.module;

import com.itinerary.block.Block;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;

@Api
public interface ModuleController {
    @ApiOperation(value = "Return all the modules.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Modules")
    })
    MappingJacksonValue modules();

    @ApiOperation(value = "Return the module with the requested id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Module"),
            @ApiResponse(code = 404, message = "Module Not Found")
    })
    ResponseEntity<Module> module(long moduleId);

    @ApiOperation(value = "Update the module with the requested id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The module has ben successfully updated"),
            @ApiResponse(code = 404, message = "Module Not Found")
    })
    ResponseEntity<Module> updateModule(long moduleId, Module module);

    @ApiOperation(value = "Add a block to the module with the requested moduleId.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The block has been successfully added to the module"),
            @ApiResponse(code = 409, message = "The block is already into the module"),
            @ApiResponse(code = 404, message = "Module or Block Not Found")
    })
    ResponseEntity<Block> addBlock(Block block, long moduleId);

    @ApiOperation(value = "Delete the block with the requested blockId from the module with the requested moduleId.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The block has been successfully deleted from the module"),
            @ApiResponse(code = 404, message = "Module or Block Not Found")
    })
    ResponseEntity<Module> deleteBlockFromModule(long moduleId, long blockId);
}
