package toyproject.syxxn.back_end.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@AllArgsConstructor
public class SignUpRequest {

    @Email
    @NotNull
    private final String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=.*[~!@#*_])[a-zA-Z0-9~!@#*_]{8,15}$")
    private final String password;

    @NotNull
    private final String nickname;

    @NotNull
    private final int age;

    @NotNull
    private final String sex;

    @NotNull
    private final boolean isExperienceRasingPet;

    private final String experience;

    @NotNull
    private final String address;

}
