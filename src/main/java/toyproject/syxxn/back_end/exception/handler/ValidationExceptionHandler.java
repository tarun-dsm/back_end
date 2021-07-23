package toyproject.syxxn.back_end.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<ErrorResponse> handleValidException() {
        return new ResponseEntity<>(new ErrorResponse(400, "Invalid value"), HttpStatus.valueOf(400));
    }

}
