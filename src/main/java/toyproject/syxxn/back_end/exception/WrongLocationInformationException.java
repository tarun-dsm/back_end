package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class WrongLocationInformationException extends TarunException {

    public static final WrongLocationInformationException EXCEPTION = new WrongLocationInformationException();

    private WrongLocationInformationException() {
        super(400, "Latitude and longitude information is incorrect.");
    }
}
