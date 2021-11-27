package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProfilePostResponse {

    List<ProfilePostDto> posts;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfilePostDto {

        private Integer id;

        private String title;

        private String firstImagePath;

        private String createdAt;

        private Boolean isApplicationEnd;

        private String protectorId;

        private String protectorNickname;

    }

}
