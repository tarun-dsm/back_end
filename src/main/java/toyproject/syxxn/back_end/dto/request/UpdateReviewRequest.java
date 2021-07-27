package toyproject.syxxn.back_end.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReviewRequest {

    @NotBlank
    private BigDecimal ratingScore;

    @NotBlank
    private String comment;

}
