package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class UserEmailAlreadyExistsException extends TarunException {
    public UserEmailAlreadyExistsException() {
        super(409, "This email already exists.");
    }
}
