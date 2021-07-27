package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class PostNotFoundException extends TarunException {
    public PostNotFoundException() {
        super(404, "Post is not found.");
    }
}
