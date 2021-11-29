package toyproject.syxxn.back_end.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=.*[~!@#*_])[a-zA-Z0-9~!@#*_]{8,15}$")
    private String password;

    @Length(max = 10)
    @NotBlank
    private String nickname;

    private int age;

    @NotBlank
    private String sex;

    private boolean isExperienceRaisingPet;

    @Length(max = 100)
    private String experience;

}
