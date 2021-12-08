package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class FileSaveFailedException extends TarunException {

    public static final FileSaveFailedException EXCEPTION = new FileSaveFailedException();

    private FileSaveFailedException() {
        super(400, "Failed to save file.");
    }
}
