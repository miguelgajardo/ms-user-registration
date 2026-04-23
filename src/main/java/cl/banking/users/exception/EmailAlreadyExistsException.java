package cl.banking.users.exception;

/**
 * Exception thrown when attempting to register a user with an email
 * that already exists in the system.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
	private static final String DEFAULT_MESSAGE = "El correo está registrado";

    public EmailAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
	
}