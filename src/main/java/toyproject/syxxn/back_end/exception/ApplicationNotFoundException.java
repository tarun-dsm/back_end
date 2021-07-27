package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class ApplicationNotFoundException extends TarunException {
    public ApplicationNotFoundException() {
        super(404, "Application not found.");
    }
}
