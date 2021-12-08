package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class ReviewNotFoundException extends TarunException {

    public static final ReviewNotFoundException EXCEPTION = new ReviewNotFoundException();

    private ReviewNotFoundException() {
        super(404, "Review is not found.");
    }
}
