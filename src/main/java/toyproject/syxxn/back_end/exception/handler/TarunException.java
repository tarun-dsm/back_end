package toyproject.syxxn.back_end.exception.handler;

import lombok.Getter;

@Getter
public class TarunException extends RuntimeException {

    private final Integer status;
    private final String message;

    protected TarunException(Integer status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

}
