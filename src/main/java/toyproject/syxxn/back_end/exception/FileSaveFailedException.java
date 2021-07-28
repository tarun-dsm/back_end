package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class FileSaveFailedException extends TarunException {
    public FileSaveFailedException() {
        super(400, "Failed to save file.");
    }
}
