package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class AfterApplicationClosedException extends TarunException {
    public AfterApplicationClosedException() {
        super(400, "Application is closed.");
    }
}
