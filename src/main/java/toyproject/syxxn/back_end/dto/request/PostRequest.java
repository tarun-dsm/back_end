package toyproject.syxxn.back_end.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequest {

    private PostRequestDto post;
    private PetRequestDto pet;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PostRequestDto {
        @NotBlank
        @Length(min = 2, max = 30)
        private String title;

        @NotBlank
        @Length(min = 2, max = 255)
        private String description;

        @NotBlank
        private String protectionStartDate;

        @NotBlank
        private String protectionEndDate;

        @NotBlank
        private String applicationEndDate;

        @NotBlank
        @Length(min = 5, max = 100)
        private String contactInfo;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PetRequestDto {
        @NotBlank
        @Length(max = 30)
        private String petName;

        @NotBlank
        @Length(max = 30)
        private String petSpecies;

        @NotBlank
        private String petSex;

        @NotBlank
        private String animalType;
    }

    @Builder
    public PostRequest(PostRequestDto post, PetRequestDto pet) {
        this.post = post;
        this.pet = pet;
    }
}
