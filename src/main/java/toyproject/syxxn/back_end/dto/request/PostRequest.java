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

    @JsonFormat(pattern = "protection_start_date")
    @NotNull
    private String protectionstartdate;

    @JsonFormat(pattern = "protection_end_date")
    @NotNull
    private String protectionenddate;

    @JsonFormat(pattern = "application_end_date")
    @NotNull
    private String applicationenddate;

    @JsonFormat(pattern = "contactinfo")
    @NotNull
    @Length(min = 5, max = 100)
    private String contactinfo;

    @JsonFormat(pattern = "pet_name")
    @NotNull
    @Length(max = 30)
    private String petname;

    @JsonFormat(pattern = "pet_species")
    @NotNull
    @Length(max = 30)
    private String petspecies;

    @JsonFormat(pattern = "pet_sex")
    @NotNull
    private String petsex;

}
