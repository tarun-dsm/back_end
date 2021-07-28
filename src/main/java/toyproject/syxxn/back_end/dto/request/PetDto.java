package toyproject.syxxn.back_end.dto.request;

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
public class PetDto {

    @NotNull
    @Length(max = 30)
    private String petName;

    @NotNull
    @Length(max = 30)
    private String petSpecies;

    @NotNull
    private String petSex;

}
