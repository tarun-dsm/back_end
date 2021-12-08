package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class FileNumberExceedException extends TarunException {

    public static final FileNumberExceedException EXCEPTION = new FileNumberExceedException();

    private FileNumberExceedException() {
        super(400, "The number of files that can be saved is exceeded.");
    }
}
