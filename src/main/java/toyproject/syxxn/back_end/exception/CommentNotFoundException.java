package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class CommentNotFoundException extends TarunException {

    public static final CommentNotFoundException EXCEPTION = new CommentNotFoundException();

    private CommentNotFoundException() {
        super(404, "Comment is not found.");
    }
}
