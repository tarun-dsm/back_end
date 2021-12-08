package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class UserEmailAlreadyExistsException extends TarunException {

    public static final UserEmailAlreadyExistsException EXCEPTION = new UserEmailAlreadyExistsException();

    private UserEmailAlreadyExistsException() {
        super(409, "This email already exists.");
    }
}
