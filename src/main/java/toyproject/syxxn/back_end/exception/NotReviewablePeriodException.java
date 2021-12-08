package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class NotReviewablePeriodException extends TarunException {

    public static final NotReviewablePeriodException EXCEPTION = new NotReviewablePeriodException();

    private NotReviewablePeriodException() {
        super(400, "It is not a reviewable period.");
    }
}
