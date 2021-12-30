package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AccountRatingResponse {

    private final String rating;

    private final BigDecimal averageScore;

}
