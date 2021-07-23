package toyproject.syxxn.back_end.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class VerifyRequest {

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String number;

}
