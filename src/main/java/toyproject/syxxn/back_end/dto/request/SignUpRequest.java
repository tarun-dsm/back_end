package toyproject.syxxn.back_end.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@AllArgsConstructor
public class SignUpRequest {

    @Email
    @NotBlank
    private final String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=.*[~!@#*_])[a-zA-Z0-9~!@#*_]{8,15}$")
    private final String password;

    @NotBlank
    private final String nickname;

    private final int age;

    @NotBlank
    private final String sex;

    private final boolean isExperienceRasingPet;

    private final String experience;

    @NotBlank
    private final String address;

}
