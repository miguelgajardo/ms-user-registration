package cl.banking.users.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import cl.banking.users.config.JwtConfiguration;
import cl.banking.users.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

/**
 * Utility class for JWT token generation.
 * Used during user registration to create the initial access token.
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtConfiguration jwtConfig;

    /**
     * Creates the signing key from the configured secret.
     */
    private SecretKey getSigningKey() {
    	return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * Generates a JWT token for a newly registered user.
     * 
     * @param user The user entity with ID, email, and name
     * @return JWT token string (header.payload.signature)
     */
    public String generateToken(User user) {
    	Map<String, Object> claims = new HashMap<>();
    	claims.put("email", user.getEmail());
    	claims.put("name", user.getName());
    	return Jwts.builder()
    			.setClaims(claims)
    			.setSubject(user.getId().toString())
    			.setIssuedAt(new Date())
    			.setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
    			.signWith(getSigningKey(), SignatureAlgorithm.HS256)
    			.compact();
    }
	
}
