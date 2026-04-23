package cl.banking.users.controller;

import static org.assertj.core.api.Assertions.assertThat;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import cl.banking.users.api.model.UserRequest;
import cl.banking.users.api.model.UserResponse;
import cl.banking.users.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UserController Integration Tests")
public class UserControllerIntegrationTest {

	private static final String BASE_URL = "/api/v1/users";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("Should register user successfully and return valid JWT token")
	void shouldRegisterUserSuccessfully() throws Exception {
		String requestJson = loadJsonFromFile("json/user-registration-request.json");
		MvcResult result = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("Juan Rodriguez"))
				.andExpect(jsonPath("$.email").value("juan@dominio.cl")).andExpect(jsonPath("$.created").exists())
				.andExpect(jsonPath("$.modified").exists()).andExpect(jsonPath("$.last_login").exists())
				.andExpect(jsonPath("$.token").exists()).andExpect(jsonPath("$.isactive").value(true))
				.andExpect(jsonPath("$.phones[0].number").value("1234567"))
				.andExpect(jsonPath("$.phones[0].citycode").value("1"))
				.andExpect(jsonPath("$.phones[0].contrycode").value("57")).andReturn();

		UserResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);
		assertThat(response.getId()).isNotNull();
		assertThat(response.getToken()).isNotEmpty();
		assertThat(response.getToken().split("\\.")).hasSize(3);
		assertThat(userRepository.existsByEmail("juan@dominio.cl")).isTrue();
		assertThat(userRepository.findByEmail("juan@dominio.cl")).isPresent();
	}

	@Test
	@DisplayName("Should return 409 when email already exists")
	void shouldReturn409WhenEmailAlreadyExists() throws Exception {
		String requestJson = loadJsonFromFile("json/user-registration-request.json");
		mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isCreated());
		mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isConflict()).andExpect(jsonPath("$.mensaje").value("El correo está registrado"));
	}

	@Test
	@DisplayName("Should return 400 when email format is invalid")
	void shouldReturn400WhenEmailFormatInvalid() throws Exception {
		String requestJson = loadJsonFromFile("json/user-registration-invalid-email.json");
		mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.mensaje").exists());
	}

	@Test
	@DisplayName("Should return 400 when password is too weak")
	void shouldReturn400WhenPasswordIsWeak() throws Exception {
		String requestJson = loadJsonFromFile("json/user-registration-weak-password.json");
		mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.mensaje").exists());
	}

	@Test
	@DisplayName("Should return 400 when required fields are missing")
	void shouldReturn400WhenRequiredFieldsMissing() throws Exception {
		String requestJson = """
				{
				    "name": "",
				    "email": "",
				    "password": "",
				    "phones": []
				}
				""";
		mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.mensaje").exists());
	}

	/**
	 * Load JSON file from test resources.
	 */
	private String loadJsonFromFile(String path) throws Exception {
		ClassPathResource resource = new ClassPathResource(path);
		return Files.readString(Path.of(resource.getURI()));
	}

}
