package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class AfterApplicationClosedException extends TarunException {

    public static final AfterApplicationClosedException EXCEPTION = new AfterApplicationClosedException();

    private AfterApplicationClosedException() {
        super(400, "Application is closed.");
    }
}
