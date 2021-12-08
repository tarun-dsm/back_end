package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class VerifyNumberNotMatchException extends TarunException {

    public static final VerifyNumberNotMatchException EXCEPTION = new VerifyNumberNotMatchException();

    private VerifyNumberNotMatchException() {
        super(400, "This verify number does not match.");
    }
}
