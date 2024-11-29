package com.app.springBack.controller;

import java.util.HashMap;
import java.util.Optional;

import com.app.springBack.model.User;
import com.app.springBack.repository.UserRepository;
import com.app.springBack.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

@RestController
@Validated
public class UserController {

	private final UserRepository userRepository;
	@Autowired 
    PasswordEncoder passwordEncoder;

	public UserController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
	}

	@GetMapping("/users")
	public Iterable<User> findAllUsers() {
		return this.userRepository.findAll();
	}

	@GetMapping("/user/{id}")
	public User findById(@PathVariable("id") int id) {
		return this.userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("User with id %d not found", id)));
	}

	@PostMapping("/register")
	public User createUser(@Valid @RequestBody User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return this.userRepository.save(user);
	}

	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/users")
	public void deleteAllUsers() {
		this.userRepository.deleteAll();
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<HashMap<String, String>> deleteUserById(@PathVariable("id") int id) {
		Optional<User> userToDelete = this.userRepository.findById(id);

		if (userToDelete.isEmpty()) {	
			// Throw a ResponseStatusException to indicate the user was not found
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", id));
		}

		// Proceed with deletion if user exists
		this.userRepository.deleteById(id);

		// Prepare the response
		HashMap<String, String> response = new HashMap<>();
		response.put("status", "204");
		response.put("message", "no body returned in 204");
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
	}

}
