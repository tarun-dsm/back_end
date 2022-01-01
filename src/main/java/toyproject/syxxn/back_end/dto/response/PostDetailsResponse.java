package toyproject.syxxn.back_end.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PostDetailsResponse {

    private final Integer writerId;

    private final String rating;

    private final String nickname;

    @JsonProperty("is_mine")
    private final boolean isMine;

    @JsonProperty("is_applied")
    private final boolean isApplied;

    private final PostDetailsDto post;

    private final PetDetailsDto pet;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostDetailsDto {

        private final String title;

        private final String description;

        private final String contactInfo;

        private final LocalDate protectionStartDate;

        private final LocalDate protectionEndDate;

        private final LocalDate applicationEndDate;

        private final String lastModifiedAt;

        private final Boolean isUpdated;

        private final Boolean isApplicationEnd;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PetDetailsDto {

        private final String petName;

        private final String petSpecies;

        private final String petSex;

        private final String animalType;

        private final List<String> filePaths;

    }

}
