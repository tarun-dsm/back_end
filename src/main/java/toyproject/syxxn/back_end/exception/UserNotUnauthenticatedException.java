package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class UserNotUnauthenticatedException extends TarunException {
    public UserNotUnauthenticatedException() {
        super(401, "User is not Authenticated.");
    }
}
