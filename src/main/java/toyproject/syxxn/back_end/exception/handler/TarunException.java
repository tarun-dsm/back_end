package toyproject.syxxn.back_end.exception.handler;

import lombok.Getter;

@Getter
public class TarunException extends RuntimeException {

    private final int status;
    private final String message;

    protected TarunException(int status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

}
