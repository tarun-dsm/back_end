package toyproject.syxxn.back_end.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String nickname;

    @NotNull
    private int age;

    @NotNull
    private String sex;

    @NotNull
    private boolean isExperienceRasingPet;

    private String experience;

    @NotNull
    private String address;

}
