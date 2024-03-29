package toyproject.syxxn.back_end.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;

@ControllerAdvice
public class TarunExceptionHandler {

    @ExceptionHandler(TarunException.class)
    protected ResponseEntity<ErrorResponse> handleTarunException(final TarunException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getStatus(), e.getMessage()), HttpStatus.valueOf(e.getStatus()));
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<ErrorResponse> handleValidException() {
        return new ResponseEntity<>(new ErrorResponse(400, "Invalid value"), HttpStatus.valueOf(400));
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException() {
        return new ResponseEntity<>(new ErrorResponse(400, "Null Pointer Exception"), HttpStatus.valueOf(400));
    }

}
