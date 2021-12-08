package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class UserNotAccessibleException extends TarunException {

    public static final UserNotAccessibleException EXCEPTION = new UserNotAccessibleException();

    private UserNotAccessibleException() {
        super(401, "User is not accessible.");
    }
}
