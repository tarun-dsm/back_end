package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class UserNicknameAlreadyExistsException extends TarunException {
    public UserNicknameAlreadyExistsException() {
        super(409, "This nickname is already exists.");
    }
}
