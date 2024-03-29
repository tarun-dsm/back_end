package toyproject.syxxn.back_end.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=.*[~!@#*_])[a-zA-Z0-9~!@#*_]{8,15}$")
    private String password;

    @Length(min = 2, max = 10)
    @NotBlank
    private String nickname;

    private int age;

    @NotBlank
    private String sex;

    @NotNull
    private Boolean isExperienceRaisingPet;

    @Length(max = 100)
    private String experience;

}
