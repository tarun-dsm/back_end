package toyproject.syxxn.back_end.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailsResponse {

    private Integer writerId;

    private String rating;

    private String nickname;

    @JsonProperty("is_mine")
    private boolean isMine;

    @JsonProperty("is_applied")
    private boolean isApplied;

    private PostDetailsDto post;

    private PetDetailsDto pet;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDetailsDto {

        private String title;

        private String description;

        private String contactInfo;

        private LocalDate protectionStartDate;

        private LocalDate protectionEndDate;

        private LocalDate applicationEndDate;

        private LocalDateTime createdAt;

        private Boolean isUpdated;

        private Boolean isApplicationEnd;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PetDetailsDto {

        private String petName;

        private String petSpecies;

        private String petSex;

        private String animalType;

        private List<String> filePaths;

    }

}
