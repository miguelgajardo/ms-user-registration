package cl.banking.users.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cl.banking.users.api.UsersApi;
import cl.banking.users.api.model.UserRequest;
import cl.banking.users.api.model.UserResponse;
import cl.banking.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for user operations. Implements the generated UsersApi
 * interface from OpenAPI spec.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

	private final UserService userService;

	/**
	 * Register a new user.
	 * 
	 * @param userRequest User registration data (validated by @Valid)
	 * @return Created user with access token
	 */
	@Override
	public UserResponse registerUser(@Valid @RequestBody UserRequest userRequest) {
		log.info("Received registration request for email: {}", userRequest.getEmail());
		UserResponse response = userService.registerUser(userRequest);
		log.info("Registration successful for user ID: {}", response.getId());
		return response;
	}

}
