package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class EmailSendException extends TarunException {

    public static final EmailSendException EXCEPTION = new EmailSendException();

    private EmailSendException() {
        super(400, "Email was not sent properly.");
    }
}
