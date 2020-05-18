package com.user;

import javax.servlet.http.HttpSession;

import com.GeneralRestController;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserRestController extends GeneralRestController implements UserController {

	@Autowired
	private ModelMapper modelMapper;

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

		User userToReturn = modelMapper.map(user, User.class);
		return new ResponseEntity<>(userToReturn, HttpStatus.CREATED);
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