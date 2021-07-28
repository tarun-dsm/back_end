package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class NotReviewablePeriodException extends TarunException {
    public NotReviewablePeriodException() {
        super(400, "It is not a reviewable period.");
    }
}
