package toyproject.syxxn.back_end.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequest {

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

    @Builder
    public PostRequest(String title, String description, String protectionStartDate, String protectionEndDate, String applicationEndDate, String contactInfo, String petName, String petSpecies, String petSex, String animalType) {
        this.title = title;
        this.description = description;
        this.protectionStartDate = protectionStartDate;
        this.protectionEndDate = protectionEndDate;
        this.applicationEndDate = applicationEndDate;
        this.contactInfo = contactInfo;
        this.petName = petName;
        this.petSpecies = petSpecies;
        this.petSex = petSex;
        this.animalType = animalType;
    }
}
