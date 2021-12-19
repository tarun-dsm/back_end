package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class ProfileResponse {

    private final String nickname;

    private final Integer age;

    private final String sex;

    private final boolean isExperienceRaisingPet;

    private final String administrationDivision;

    private final String experience;

    private final boolean isLocationConfirm;

    private final BigDecimal avgGrade;

    private final String rating;

}
