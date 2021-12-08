package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class UserAlreadyRegisteredException extends TarunException {

    public static final UserAlreadyRegisteredException EXCEPTION = new UserAlreadyRegisteredException();

    private UserAlreadyRegisteredException() {
        super(409, "User already registered.");
    }
}
