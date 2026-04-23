package cl.banking.users.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {
	
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Test
	void shouldEncodeAndMatchPassword() {
		String rawPass = "Hunter12";
		String encodedPass = passwordEncoder.encode(rawPass);
		assertThat(encodedPass).isNotEqualTo(rawPass);
		assertThat(passwordEncoder.matches(rawPass, encodedPass)).isTrue();
	}
	
	@Test
	void shouldGenerateDifferentHashForSamePassword() {
		
		String rawPass = "Hunter12";
        String encoded1 = passwordEncoder.encode(rawPass);
        String encoded2 = passwordEncoder.encode(rawPass);
        assertThat(encoded1).isNotEqualTo(encoded2);
        assertThat(passwordEncoder.matches(rawPass, encoded1)).isTrue();
        assertThat(passwordEncoder.matches(rawPass, encoded2)).isTrue();
	}
	
}
