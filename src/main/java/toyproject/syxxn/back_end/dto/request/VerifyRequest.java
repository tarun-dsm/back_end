package toyproject.syxxn.back_end.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String number;

}
