package vn.nhannt.jobhunter.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.nhannt.jobhunter.domain.RestResponse;

/**
 * AOP
 * Java v4.3
 */
@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(IdInvalidException ide) {
        RestResponse<Object> response = new RestResponse<>();

        response.setMessage("Id Invalid Exception");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(ide.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public ResponseEntity<RestResponse<Object>> handleNumberFormatException(NumberFormatException nfe) {
        RestResponse<Object> response = new RestResponse<>();

        response.setMessage("Number Format Exception");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(nfe.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
