package toyproject.syxxn.back_end.exception.handler;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final int status;
    private final String message;

    protected ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\t\"status\": " + status +
                ",\n\t\"message\": \"" + message + '\"' +
                "\n}";
    }

}
