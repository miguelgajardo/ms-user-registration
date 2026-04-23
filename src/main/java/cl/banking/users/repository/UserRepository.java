package cl.banking.users.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.banking.users.domain.User;

/**
 * Repository for User entity operations.
 * Provides database access for user management.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	 /**
     * Find a user by their email address.
     * Used for duplicate email validation during registration.
     *
     * @param email the email to search for
     * @return Optional containing the user if found
     */
	Optional<User> findByEmail(String email);
	
	 /**
     * Check if a user exists with the given email.
     * More efficient than findByEmail when only existence check is needed.
     *
     * @param email the email to check
     * @return true if a user with this email exists
     */
	boolean existsByEmail(String email);
	
}
