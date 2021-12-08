package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class InvalidScheduleSettingException extends TarunException {

    public static final InvalidScheduleSettingException EXCEPTION = new InvalidScheduleSettingException();

    private InvalidScheduleSettingException() {
        super(400, "Check the schedule set in the post.");
    }
}
