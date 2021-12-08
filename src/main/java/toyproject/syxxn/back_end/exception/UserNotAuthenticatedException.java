package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class UserNotAuthenticatedException extends TarunException {

    public static final UserNotAuthenticatedException EXCEPTION = new UserNotAuthenticatedException();

    private UserNotAuthenticatedException() {
        super(401, "User is not Authenticated.");
    }
}
