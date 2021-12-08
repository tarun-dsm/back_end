package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class InvalidFileExtensionException extends TarunException {

    public static final InvalidFileExtensionException EXCEPTION = new InvalidFileExtensionException();

    private InvalidFileExtensionException() {
        super(400, "File type is invalid.");
    }
}
