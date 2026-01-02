package patil.parshwa.blog.error;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ----------------- User & Auth Exceptions -----------------
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUsernameNotFound(UsernameNotFoundException ex) {
        return new ResponseEntity<>(new ApiError("USER_NOT_FOUND", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        return new ResponseEntity<>(new ApiError("BAD_CREDENTIALS", "Invalid username or password"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>(new ApiError("AUTHENTICATION_FAILED", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseEntity<>(new ApiError("ACCESS_DENIED", "You don't have permission to perform this action"), HttpStatus.FORBIDDEN);
    }

    // ----------------- JWT Exceptions -----------------
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleExpiredJwt(ExpiredJwtException ex) {
        return new ResponseEntity<>(new ApiError("TOKEN_EXPIRED", "Your session has expired. Please log in again."), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({MalformedJwtException.class, SignatureException.class})
    public ResponseEntity<ApiError> handleInvalidJwt(RuntimeException ex) {
        return new ResponseEntity<>(new ApiError("TOKEN_INVALID", "Invalid token"), HttpStatus.UNAUTHORIZED);
    }

    // ----------------- Request Validation -----------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationError(MethodArgumentNotValidException ex) {
        // Return only the first validation error
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Invalid request");

        return new ResponseEntity<>(new ApiError("VALIDATION FAILED", errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiError> handleMissingHeader(MissingRequestHeaderException ex) {
        return new ResponseEntity<>(new ApiError("MISSING_HEADER", ex.getHeaderName() + " header is required"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ApiError> handleNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
        return new ResponseEntity<>(new ApiError("NOT_ACCEPTABLE", "Requested media type is not supported by this endpoint"), HttpStatus.NOT_ACCEPTABLE);
    }

    // ----------------- Fallback Exception -----------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ApiError("INTERNAL_ERROR", "An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ----------------- ResourceNotFound Class -----------------
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ApiError("RESOURCE_NOT_FOUND", ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
