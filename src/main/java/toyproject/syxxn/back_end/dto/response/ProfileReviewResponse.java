package toyproject.syxxn.back_end.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProfileReviewResponse {

    List<ProfileReviewDto> reviews;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileReviewDto {

        private Integer id;

        private String nickname;

        private BigDecimal grade;

        private String comment;

        private LocalDateTime createdAt;

        @JsonProperty("is_my_review")
        private boolean isMyReview;

    }

}
