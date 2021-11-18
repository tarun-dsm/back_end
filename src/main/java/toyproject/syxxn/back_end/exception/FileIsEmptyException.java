package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class FileIsEmptyException extends TarunException {
    public FileIsEmptyException() {
        super(400, "File is empty.");
    }
}
