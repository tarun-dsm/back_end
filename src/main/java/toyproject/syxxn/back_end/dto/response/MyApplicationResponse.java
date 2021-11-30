package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyApplicationResponse {

    private List<MyApplicationDto> myApplications;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyApplicationDto {

        private Integer id;

        private String postName;

        private Integer postId;

        private boolean isAccepted;

        private boolean isEnd;

        private String firstImagePath;

        private String startDate;

        private String endDate;

        private String administrationDivision;

    }

}
