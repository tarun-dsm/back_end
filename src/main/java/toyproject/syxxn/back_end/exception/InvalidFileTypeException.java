package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class InvalidFileTypeException extends TarunException {
    public InvalidFileTypeException() {
        super(400, "File type is invalid.");
    }
}
