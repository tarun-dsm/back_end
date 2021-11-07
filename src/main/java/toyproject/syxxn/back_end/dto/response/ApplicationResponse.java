package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {

    private List<ApplicationDto> applications;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationDto {

        private Integer applicationId;

        private Integer applicantId;

        private String applicantNickname;

        private Boolean isAccepted;

        private LocalDateTime applicationDate;

        private String administrationDivision;

    }

}
