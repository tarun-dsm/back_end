package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProfilePostResponse {

    private final List<ProfilePostDto> posts;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ProfilePostDto {

        private final Integer id;

        private final String title;

        private final String firstImagePath;

        private final String lastModifiedAt;

        private final Boolean isApplicationEnd;

        private final String protectorId;

        private final String protectorNickname;

    }

}
