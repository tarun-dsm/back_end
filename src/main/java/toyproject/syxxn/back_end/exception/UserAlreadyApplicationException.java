package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class UserAlreadyApplicationException extends TarunException {
    public UserAlreadyApplicationException() {
        super(409, "User have already applied for protection.");
    }
}
