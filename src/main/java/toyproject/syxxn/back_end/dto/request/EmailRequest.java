package toyproject.syxxn.back_end.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
@AllArgsConstructor
public class EmailRequest {

    @Email
    private final String email;

}
