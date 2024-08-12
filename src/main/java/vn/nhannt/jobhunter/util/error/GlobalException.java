package vn.nhannt.jobhunter.util.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import vn.nhannt.jobhunter.domain.response.RestResponse;

/**
 * AOP
 * Java v4.3
 * Handle exceptions at the controller
 */
@RestControllerAdvice
public class GlobalException {

    /**
     * Jwt configuration allows to handle exceptions at the filter chain
     *
     * @param: UsernameNotFoundException, BadCredentialsException
     * @return: ResponseEntity
     */
    @ExceptionHandler(value = {
            // IdInvalidException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            // NumberFormatException.class

    })
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(Exception e) {

        RestResponse<Object> response = new RestResponse<>();

        response.setMessage("Bad Credentials Exception");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setError("Username hoặc Password không hợp lệ");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
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
        response.setMessage("Method Argument Not Valid Exception");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = { NumberFormatException.class })
    public ResponseEntity<RestResponse<Object>> handleNumberFormatException(NumberFormatException nfe) {

        RestResponse<Object> response = new RestResponse<>();

        response.setMessage("Number Format Exception");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(nfe.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = { UniqueException.class })
    public ResponseEntity<RestResponse<Object>> handleUniqueException(UniqueException ue) {

        RestResponse<Object> response = new RestResponse<>();

        response.setMessage("Unique Exception");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(ue.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = { NoResourceFoundException.class })
    public ResponseEntity<RestResponse<Object>> handleNoResourceFoundException(NoResourceFoundException nrfe) {

        RestResponse<Object> response = new RestResponse<>();

        response.setMessage("No Resource Found Exception");
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setError(nrfe.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = { JwtException.class })
    public ResponseEntity<RestResponse<Object>> handleJwtException(JwtException je) {

        RestResponse<Object> response = new RestResponse<>();

        response.setMessage("Jwt Exception");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setError(je.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = { MissingRequestCookieException.class })
    public ResponseEntity<RestResponse<Object>> handleMissingRequestCookieException(
            MissingRequestCookieException mrce) {

        RestResponse<Object> response = new RestResponse<>();

        response.setMessage("Missing Request Cookie Exception");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(mrce.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseEntity<RestResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException iae) {

        RestResponse<Object> response = new RestResponse<>();

        response.setMessage("Illegal Argument Exception");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setError(iae.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<RestResponse<Object>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException hrmnse) {

        RestResponse<Object> response = new RestResponse<>();

        response.setMessage("Http Request Method NotSupported Exception");
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        response.setError(hrmnse.getMessage());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    public ResponseEntity<RestResponse<Object>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException hmnre) {

        RestResponse<Object> response = new RestResponse<>();

        response.setMessage("Http Message Not Readable Exception");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(hmnre.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = { StorageException.class })
    public ResponseEntity<RestResponse<Object>> handleUploadFileException(
            StorageException ule) {

        RestResponse<Object> response = new RestResponse<>();

        response.setMessage("Upload File Exception");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(ule.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = { MaxUploadSizeExceededException.class })
    public ResponseEntity<RestResponse<Object>> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException musee) {

        RestResponse<Object> response = new RestResponse<>();

        response.setMessage("Upload File Exception");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(musee.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
