package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class PasswordNotMatchedException extends TarunException {
    public static final PasswordNotMatchedException EXCEPTION = new PasswordNotMatchedException();

    private PasswordNotMatchedException() {
        super(400, "Password is not matched.");
    }
}
