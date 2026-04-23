package cl.banking.users.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.banking.users.config.JwtConfiguration;
import cl.banking.users.domain.User;

/**
 * Unit tests for JwtUtil.
 * Verifies token generation for newly registered users.
 */
public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private JwtConfiguration jwtConfig;
    
    @BeforeEach
    void setUp() {
        jwtConfig = new JwtConfiguration();
        jwtConfig.setSecret("7f3e4a2c1b8d9f0e5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a");
        jwtConfig.setExpiration(3600000L);
        jwtUtil = new JwtUtil(jwtConfig);
    }
    
    @Test
    void shouldGenerateValidToken() {
    	User user = User.builder()
    			.id(UUID.randomUUID())
    			.email("juan@dominio.cl")
    			.name("Juan Rodriguez")
    			.build();
        String token = jwtUtil.generateToken(user);
        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3);
    }
}
