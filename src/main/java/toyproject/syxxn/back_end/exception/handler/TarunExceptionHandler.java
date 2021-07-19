package toyproject.syxxn.back_end.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TarunExceptionHandler {

    @ExceptionHandler(TarunException.class)
    protected ResponseEntity<ErrorResponse> handleDiarioException(final TarunException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getStatus(), e.getMessage()), HttpStatus.valueOf(e.getStatus()));
    }

}
