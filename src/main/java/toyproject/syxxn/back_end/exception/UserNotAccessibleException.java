package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class UserNotAccessibleException extends TarunException {
    public UserNotAccessibleException() {
        super(401, "User is not accessible.");
    }
}
