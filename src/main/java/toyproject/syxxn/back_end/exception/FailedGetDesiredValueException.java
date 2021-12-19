package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class FailedGetDesiredValueException extends TarunException {

    public static final FailedGetDesiredValueException EXCEPTION = new FailedGetDesiredValueException();

    private FailedGetDesiredValueException() {
        super(500, "Failed to get the desired value from Kakao API response.");
    }
}
