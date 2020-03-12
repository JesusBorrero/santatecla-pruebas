package com.user;

import javax.servlet.http.HttpSession;

import com.GeneralRestController;

import com.course.Course;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserRestController extends GeneralRestController {
    
	@GetMapping(value="/login")
	public ResponseEntity<User> logIn() {
        if (!userComponent.isLoggedUser()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
			User loggedUser = userComponent.getLoggedUser();
            return new ResponseEntity<>(loggedUser, HttpStatus.OK);
        }
	}

	@PostMapping(value="/register")
	public ResponseEntity<User> register(@RequestParam("name") String name, @RequestParam("authdata") String password) {

		User findUser = userService.findByName(name);

		//If the user is already sign in or already exists
		if((userComponent.getLoggedUser() != null || findUser != null) && !userComponent.isAdmin()){
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		User user = new User();
		user.setName(name);
		user.setPasswordHash(password);
		userService.save(user);

		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@GetMapping(value="/logout")
	public ResponseEntity<Boolean> logOut(HttpSession session) {
		if (!userComponent.isLoggedUser()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			session.invalidate();
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
	}
}