package toyproject.syxxn.back_end.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UpdateReviewRequest {

    @NotNull
    private final BigDecimal ratingScore;

    @NotNull
    private final String comment;

}
