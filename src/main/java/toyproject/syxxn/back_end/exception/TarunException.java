package toyproject.syxxn.back_end.exception;

import lombok.Getter;

@Getter
public class TarunException extends RuntimeException {

    private Integer status;
    private String message;

    public TarunException(Integer status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

}
