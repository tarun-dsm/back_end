package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class InvalidTokenException extends TarunException {
    public InvalidTokenException() {
        super(401, "This token is invalid.");
    }
}
