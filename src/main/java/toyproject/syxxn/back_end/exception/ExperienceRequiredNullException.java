package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class ExperienceRequiredNullException extends TarunException {

    public static final ExperienceRequiredNullException EXCEPTION = new ExperienceRequiredNullException();

    private ExperienceRequiredNullException() {
        super(418, "When the value 'isExperienceRasingPet' is false, the value 'experience' must be null.");
    }
}
