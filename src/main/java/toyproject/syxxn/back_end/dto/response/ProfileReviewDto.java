package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileReviewDto {

    private Integer id;

    private String nickname;

    private BigDecimal grade;

    private String comment;

    private LocalDateTime createdAt;

    private Boolean isMyReview;

}
