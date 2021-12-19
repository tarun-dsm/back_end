package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MyApplicationResponse {

    private final List<MyApplicationDto> myApplications;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MyApplicationDto {

        private final Integer id;

        private final String postName;

        private final Integer postId;

        private final Boolean isAccepted;

        private final Boolean isEnd;

        private final Boolean isWrittenReview;

        private final String firstImagePath;

        private final String startDate;

        private final String endDate;

        private final String administrationDivision;

    }

}
