package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class ApplicationResponse {

    private final List<ApplicationDto> applications;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ApplicationDto {

        private final Integer applicationId;

        private final Integer applicantId;

        private final String applicantNickname;

        private final Boolean isAccepted;

        private final String applicationDate;

        private final String administrationDivision;

        private final Boolean isWrittenReview;

    }

}
