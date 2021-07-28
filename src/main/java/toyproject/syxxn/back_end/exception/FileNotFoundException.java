package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class FileNotFoundException extends TarunException {
    public FileNotFoundException() {
        super(400, "File is not found.");
    }
}
