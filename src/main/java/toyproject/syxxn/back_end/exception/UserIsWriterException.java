package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class UserIsWriterException extends TarunException {
    public UserIsWriterException() {
        super(400, "Writer does not have permission to apply.");
    }
}
