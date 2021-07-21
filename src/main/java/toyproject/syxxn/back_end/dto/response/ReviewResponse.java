package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponse {

    private final Long id;

    private final Long writerId;

    private final Long targetId;

    private final String comment;

    private final BigDecimal ratingScore;

    private final LocalDateTime createdAt;

}
