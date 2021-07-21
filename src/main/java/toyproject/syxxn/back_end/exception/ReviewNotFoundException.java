package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class ReviewNotFoundException extends TarunException {
    public ReviewNotFoundException() {
        super(404, "Review is not found.");
    }
}
