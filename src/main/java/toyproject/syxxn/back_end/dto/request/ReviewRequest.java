package toyproject.syxxn.back_end.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ReviewRequest {

    @NotBlank
    private final Long targetId;

    @NotBlank
    private final BigDecimal ratingScore;

    @NotBlank
    private final String comment;

}
