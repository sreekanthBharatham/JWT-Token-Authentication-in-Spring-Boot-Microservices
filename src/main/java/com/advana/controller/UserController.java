package com.advana.controller;

import com.advana.model.User;
import com.advana.config.JwtGeneratorInterface;
import com.advana.service.UserService;
import com.advana.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
	
	private UserService userService;
	private JwtGeneratorInterface jwtGenerator;
	
	@Autowired
	public UserController(UserService userService, JwtGeneratorInterface jwtGenerator){
        this.userService=userService;
        this.jwtGenerator=jwtGenerator;
    }
	
	 @PostMapping("/register")
	 public ResponseEntity<?> postUser(@RequestBody User user){
		 try {
			userService.saveUser(user);
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	 }
	 
	 
	 @PostMapping("/login")
	    public ResponseEntity<?> loginUser(@RequestBody User user) {
	        try {
	            if(user.getUserName() == null || user.getPassword() == null) {
	                throw new UserNotFoundException("UserName or Password is Empty");
	            }
	            User userData = userService.getUserByNameAndPassword(user.getUserName(), user.getPassword());
	            if(userData == null){
	                throw new UserNotFoundException("UserName or Password is Invalid");
	            }
	            return new ResponseEntity<>(jwtGenerator.generateToken(user), HttpStatus.OK);
	        } catch (UserNotFoundException e) {
	            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	        }
	    }

}
