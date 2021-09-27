package toyproject.syxxn.back_end.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @NotNull
    @Length(min = 2, max = 30)
    private String title;

    @NotNull
    @Length(min = 2, max = 255)
    private String description;

    @NotNull
    private String protectionStartDate;

    @NotNull
    private String protectionEndDate;

    @NotNull
    private String applicationEndDate;

    @NotNull
    @Length(min = 5, max = 100)
    private String contactInfo;

    @NotNull
    @Length(max = 30)
    private String petName;

    @NotNull
    @Length(max = 30)
    private String petSpecies;

    @NotNull
    private String petSex;

    @NotNull
    private String animalType;

}
