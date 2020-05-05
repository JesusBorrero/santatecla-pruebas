package com.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

@Api
public interface UserController {

    @ApiOperation(value = "Return an user. It has to be registered")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Logged user")})
    ResponseEntity<User> logIn();

    @ApiOperation(value = "Register an user and return it.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Registered user")})
    ResponseEntity<User> register(String name, String password);

    @ApiOperation(value = "Application log out")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "It returns 'true' if everything goes OK, else it returns 'false'")
    })
    ResponseEntity<Boolean> logOut(HttpSession session);
}
