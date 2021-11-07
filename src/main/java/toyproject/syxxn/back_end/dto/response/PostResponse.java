package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostResponse {

    List<PostResponseDto> posts;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostResponseDto {

        private Integer id;

        private String title;

        private String animalType;

        private String administrationDivision;

        private String firstImagePath;

        private LocalDate protectionStartDate;

        private LocalDate protectionEndDate;

    }

}
