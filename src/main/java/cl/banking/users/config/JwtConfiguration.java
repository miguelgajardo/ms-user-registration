package cl.banking.users.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "ms-user-registration.auth.token")
@Data
public class JwtConfiguration {

	    private String secret = "mySuperSecretKeyForBankingChallenge2024WithEnoughLengthForHS256";
	    private Long expiration = 86400000L;

}