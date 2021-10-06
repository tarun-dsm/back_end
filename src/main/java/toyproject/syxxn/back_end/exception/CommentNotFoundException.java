package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class CommentNotFoundException extends TarunException {
    public CommentNotFoundException() {
        super(404, "Comment is not found.");
    }
}
