package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class FileNumberExceedException extends TarunException {
    public FileNumberExceedException() {
        super(400, "The number of files that can be saved is exceeded.");
    }
}
