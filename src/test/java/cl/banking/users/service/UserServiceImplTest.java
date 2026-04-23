package cl.banking.users.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import cl.banking.users.api.model.UserRequest;
import cl.banking.users.domain.User;
import cl.banking.users.exception.EmailAlreadyExistsException;
import cl.banking.users.mapper.UserMapper;
import cl.banking.users.repository.UserRepository;
import cl.banking.users.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserMapper userMapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private UserServiceImpl userService;

	@Test
	void shouldThrowExceptionWhenEmailAlreadyExists() {
		UserRequest request = new UserRequest();
		request.setEmail("juan@dominio.cl");
		when(userRepository.existsByEmail("juan@dominio.cl")).thenReturn(true);
		assertThatThrownBy(() -> userService.registerUser(request)).isInstanceOf(EmailAlreadyExistsException.class)
				.hasMessage("El correo está registrado");

		verify(userRepository, never()).save(any());
	}

	@Test
	void shouldRegisterUserSuccessfully() {
		UserRequest request = new UserRequest();
		request.setEmail("juan@dominio.cl");
		request.setPassword("Hunter12");
		User user = new User();
		user.setEmail("juan@dominio.cl");
		User savedUser = new User();
		savedUser.setId(java.util.UUID.randomUUID());
		when(userRepository.existsByEmail("juan@dominio.cl")).thenReturn(false);
		when(userMapper.toEntity(request)).thenReturn(user);
		when(passwordEncoder.encode("Hunter12")).thenReturn("encodedPassword");
		when(userRepository.save(user)).thenReturn(savedUser);
		when(jwtUtil.generateToken(savedUser)).thenReturn("jwt-token");
		when(userRepository.save(savedUser)).thenReturn(savedUser);
		when(userMapper.toResponse(savedUser)).thenReturn(null);
		userService.registerUser(request);
		verify(userRepository).existsByEmail("juan@dominio.cl");
		verify(passwordEncoder).encode("Hunter12");
		verify(jwtUtil).generateToken(savedUser);
	}
}