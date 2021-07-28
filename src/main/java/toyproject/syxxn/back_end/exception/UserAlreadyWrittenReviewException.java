package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class UserAlreadyWrittenReviewException extends TarunException {
    public UserAlreadyWrittenReviewException() {
        super(409, "User have already written a review.");
    }
}
