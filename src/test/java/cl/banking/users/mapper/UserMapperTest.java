package cl.banking.users.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import cl.banking.users.api.model.PhoneRequest;
import cl.banking.users.api.model.UserRequest;
import cl.banking.users.api.model.UserResponse;
import cl.banking.users.domain.Phone;
import cl.banking.users.domain.User;

public class UserMapperTest {
	
	private UserMapper userMapper;
	
	@BeforeEach
	void SetUp() {
		userMapper = Mappers.getMapper(UserMapper.class);
	}
	
	@Test
	void shouldMapPhoneRequestToEntity_CorrectingContrycodeTypo() {
		//DTO
		PhoneRequest phoneRequest = new PhoneRequest();
		phoneRequest.setNumber("123456789");
		phoneRequest.setCitycode("9");
		phoneRequest.setContrycode("56");
		//Entity
		Phone phone = userMapper.toEntity(phoneRequest);
		assertThat(phone.getNumber()).isEqualTo("123456789");
		assertThat(phone.getCityCode()).isEqualTo("9");
        assertThat(phone.getCountryCode()).isEqualTo("56");
	}
	
	@Test
	void shouldMapPhoneEntityToDto_PreservingContrycodeTypo() {
		//Entity
		Phone phone = Phone.builder()
				.number("123456789")
				.cityCode("9")
				.countryCode("56")
				.build();
		//DTO
		PhoneRequest phoneRequest = userMapper.toDto(phone);
		assertThat(phoneRequest.getNumber()).isEqualTo("123456789");
		assertThat(phoneRequest.getCitycode()).isEqualTo("9");
		assertThat(phoneRequest.getContrycode()).isEqualTo("56");
	}
	
	@Test
	void shouldMapUserRequestToEntity() {
		//Phone DTO
		PhoneRequest phoneRequest = new PhoneRequest();
		phoneRequest.setNumber("123456789");
		phoneRequest.setCitycode("9");
		phoneRequest.setContrycode("56");
		//User DTO
		UserRequest userRequest = new UserRequest();
		userRequest.setName("Juan Rodriguez");
		userRequest.setEmail("juan@dominio.cl");
		userRequest.setPassword("Hunter12");
		userRequest.setPhones(List.of(phoneRequest));
		//Entity
		User user = userMapper.toEntity(userRequest);
		assertThat(user.getName()).isEqualTo("Juan Rodriguez");
		assertThat(user.getEmail()).isEqualTo("juan@dominio.cl");
		assertThat(user.getPassword()).isEqualTo("Hunter12");
		assertThat(user.getPhones()).hasSize(1);
	    assertThat(user.getPhones().get(0).getCountryCode()).isEqualTo("56");
	}
	
	@Test
	void shouldMapUserEntityToResponse() {
		//Phone Entity
		Phone phone = Phone.builder()
				.number("123456789")
				.cityCode("9")
				.countryCode("56")
				.build();
		//User Entity
		UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
		User user = User.builder()
				.id(uuid)
				.name("Juan Rodriguez")
				.email("juan@dominio.cl")
				.createdAt(now)
				.modifiedAt(now)
				.lastLogin(now)
				.token("eyJhbGciOiJIUzI1NiJ9...")
				.isActive(true)
				.phones(List.of(phone))
				.build();
		//DTO
		UserResponse userResponse = userMapper.toResponse(user);
		assertThat(userResponse.getId()).isEqualTo(uuid);
		assertThat(userResponse.getName()).isEqualTo("Juan Rodriguez");
		assertThat(userResponse.getEmail()).isEqualTo("juan@dominio.cl");
        assertThat(userResponse.getToken()).isEqualTo("eyJhbGciOiJIUzI1NiJ9...");
        assertThat(userResponse.getIsactive()).isTrue();
        assertThat(userResponse.getPhones()).hasSize(1);
        assertThat(userResponse.getPhones().get(0).getContrycode()).isEqualTo("56");
        assertThat(userResponse.getCreated()).isNotNull();
        assertThat(userResponse.getModified()).isNotNull();
        assertThat(userResponse.getLastLogin()).isNotNull();

	}
 
}
