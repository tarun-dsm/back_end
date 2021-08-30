package toyproject.syxxn.back_end.exception;

import toyproject.syxxn.back_end.exception.handler.TarunException;

public class BlockedUserException extends TarunException {
    public BlockedUserException() {
        super(403, "This user is blocked.");
    }
}
