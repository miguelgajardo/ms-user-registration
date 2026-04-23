package cl.banking.users.service;

import cl.banking.users.api.model.UserRequest;
import cl.banking.users.api.model.UserResponse;

/**
 * Service contract for user operations.
 */
public interface UserService {

	/**
	 * Register a new user in the system.
	 *
	 * @param request User registration data
	 * @return Response with user data and access token
	 * @throws EmailAlreadyExistsException if email is already registered
	 */
	UserResponse registerUser(UserRequest userRequest);

}