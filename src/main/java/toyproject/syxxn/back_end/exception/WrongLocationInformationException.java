package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class WrongLocationInformationException extends TarunException {
    public WrongLocationInformationException() {
        super(400, "Latitude and longitude information is incorrect.");
    }
}
