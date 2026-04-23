package cl.banking.users.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.banking.users.api.model.UserRequest;
import cl.banking.users.api.model.UserResponse;
import cl.banking.users.domain.User;
import cl.banking.users.exception.EmailAlreadyExistsException;
import cl.banking.users.mapper.UserMapper;
import cl.banking.users.repository.UserRepository;
import cl.banking.users.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of UserService for user registration.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	@Transactional
	public UserResponse registerUser(UserRequest userRequest) {
		log.info("Registering new user with email: {}", userRequest.getEmail());
		if (userRepository.existsByEmail(userRequest.getEmail())) {
			log.warn("Email already registered: {}", userRequest.getEmail());
			throw new EmailAlreadyExistsException();
		}
		User user = userMapper.toEntity(userRequest);
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		User savedUser = userRepository.save(user);
		log.info("User saved with ID: {}", savedUser.getId());
		String token = jwtUtil.generateToken(savedUser);
		savedUser.setToken(token);
		userRepository.save(savedUser);
		log.info("Token generated and saved for user: {}", savedUser.getId());
		return userMapper.toResponse(savedUser);
	}

}
