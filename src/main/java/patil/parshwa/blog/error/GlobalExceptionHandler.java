package patil.parshwa.blog.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUsernameNotFoundException(UsernameNotFoundException exception){
        ApiError apiError = new ApiError("USER_NOT_FOUND", exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    // Authentication-related exceptions
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        ApiError apiError = new ApiError("BAD_CREDENTIALS", "Invalid username or password");
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
        ApiError apiError = new ApiError("AUTHENTICATION_FAILED", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    // Authorization exception
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        ApiError apiError = new ApiError("ACCESS_DENIED", "You don't have permission to perform this action");
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    // JWT-specific exceptions
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleExpiredJwt(ExpiredJwtException ex) {
        ApiError apiError = new ApiError("TOKEN_EXPIRED", "Your session has expired. Please log in again.");
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({MalformedJwtException.class, SignatureException.class})
    public ResponseEntity<ApiError> handleInvalidJwt(RuntimeException ex) {
        ApiError apiError = new ApiError("TOKEN_INVALID", "Invalid token");
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    // Request validation and parsing
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        ApiError apiError = new ApiError("VALIDATION_ERROR", String.join("; ", errors));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiError apiError = new ApiError("TYPE_MISMATCH", "Invalid value for parameter: " + ex.getName());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParameter(MissingServletRequestParameterException ex) {
        ApiError apiError = new ApiError("MISSING_PARAMETER", ex.getParameterName() + " is required");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiError> handleMissingHeader(MissingRequestHeaderException ex) {
        ApiError apiError = new ApiError("MISSING_HEADER", ex.getHeaderName() + " header is required");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException ex) {
        ApiError apiError = new ApiError("MALFORMED_JSON", "Request body is invalid or malformed");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ApiError> handleNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
        ApiError apiError = new ApiError("NOT_ACCEPTABLE", "Requested media type is not supported by this endpoint");
        return new ResponseEntity<>(apiError, HttpStatus.NOT_ACCEPTABLE);
    }

    // Fallback handler
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        ApiError apiError = new ApiError("INTERNAL_ERROR", "An unexpected error occurred");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
