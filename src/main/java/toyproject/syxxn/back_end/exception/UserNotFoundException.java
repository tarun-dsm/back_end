package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class UserNotFoundException extends TarunException {
    public UserNotFoundException() {
        super(404, "User is not found.");
    }
}
