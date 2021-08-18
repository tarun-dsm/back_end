package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private String nickname;

    private Integer age;

    private String sex;

    private boolean isExperienceRasingPet;

    private String administrationDivision;

    private String experience;

    private boolean isLocationConfirm;

    private BigDecimal avgGrade;

    private String rating;

}
