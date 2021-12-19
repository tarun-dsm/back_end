package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostResponse {

    private final List<PostResponseDto> posts;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostResponseDto {

        private final Integer id;

        private final String title;

        private final String animalType;

        private final String administrationDivision;

        private final String firstImagePath;

        private final LocalDate protectionStartDate;

        private final LocalDate protectionEndDate;

    }

}
