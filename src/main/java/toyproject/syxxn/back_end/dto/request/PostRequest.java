package toyproject.syxxn.back_end.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

}
