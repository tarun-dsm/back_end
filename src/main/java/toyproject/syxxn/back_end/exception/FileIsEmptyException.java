package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class FileIsEmptyException extends TarunException {

    public static final FileIsEmptyException EXCEPTION = new FileIsEmptyException();

    private FileIsEmptyException() {
        super(400, "File is empty.");
    }
}
