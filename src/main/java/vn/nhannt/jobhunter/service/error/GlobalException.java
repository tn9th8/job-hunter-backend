package vn.nhannt.jobhunter.service.error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.nhannt.jobhunter.domain.RestResponse;

/**
 * AOP
 * Java v4.3
 */
@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {
            IdInvalidException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            NumberFormatException.class

    })
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(IdInvalidException ide) {

        RestResponse<Object> response = new RestResponse<>();

        response.setMessage("Exception occurs");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(ide.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException manve) {

        // Map<String, String> errors = new HashMap<>();
        // manve.getBindingResult().getAllErrors().forEach((error) -> {
        // String fieldName = ((FieldError) error).getField();
        // String errorMessage = error.getDefaultMessage();
        // errors.put(fieldName, errorMessage);
        // });
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body();

        BindingResult result = manve.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        /**
         * convert List to Stream so as to use Lambda
         * map every item to DefaultMessage
         * convert back to List
         */
        List<String> errors = fieldErrors.stream()
                .map(fe -> fe.getDefaultMessage())
                .collect(Collectors.toList());

        RestResponse<Object> response = new RestResponse<>();
        response.setMessage("Exception occurs");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
