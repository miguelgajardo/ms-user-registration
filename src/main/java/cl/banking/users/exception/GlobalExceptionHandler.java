package cl.banking.users.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cl.banking.users.api.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for REST API. Mandatory: All error responses follow
 * the format: { "mensaje": "..." }
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handle email already exists exception. Returns 409 Conflict.
	 */
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
		log.warn("Email already registered: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
	}

	/**
	 * Handle validation errors from @Valid annotations. Returns 400 Bad Request
	 * with first validation error message.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
		String field = ex.getBindingResult().getFieldErrors().stream().findFirst().map(error -> error.getField())
				.orElse("");
		String fieldName = field.contains(".") ? field.substring(field.lastIndexOf(".") + 1) : field;

		String message = switch (fieldName) {
		case "email" -> "El formato del correo no es válido";
		case "password" -> "El formato de la contraseña no es válido";
		case "name" -> "El formato del nombre no es válido";
		case "number" -> "El número de teléfono no es válido";
		case "citycode" -> "El código de ciudad no es válido";
		case "contrycode" -> "El código de país no es válido";
		default -> ex.getBindingResult().getFieldErrors().stream().findFirst().map(error -> error.getDefaultMessage())
				.orElse("Error de validación");
		};
		log.warn("Validation failed for field '{}': {}", field, message);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(message));
	}

	/**
	 * Handle generic exceptions. Returns 500 Internal Server Error.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex) {
		log.error("Unexpected error ocurred: {}", ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error interno del servidor"));
	}
	
	/**
	 * Handle malformed JSON. Returns 400 Bad Request.
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleJsonMalFormado(HttpMessageNotReadableException ex) {
	    log.warn("JSON mal formado: {}", ex.getMessage());
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body(new ErrorResponse("El formato del JSON no es válido"));
	}

}
