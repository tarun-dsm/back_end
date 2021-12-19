package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProfileReviewResponse {

    private final List<ProfileReviewDto> reviews;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ProfileReviewDto {

        private final Integer id;

        private final String nickname;

        private final BigDecimal grade;

        private final String comment;

        private final String createdAt;

        private final Boolean isMyReview;

    }

}
