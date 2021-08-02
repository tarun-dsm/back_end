package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class InvalidScheduleSettingException extends TarunException {
    public InvalidScheduleSettingException() {
        super(400, "Check the schedule set in the post.");
    }
}
