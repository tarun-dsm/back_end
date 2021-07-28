package toyproject.syxxn.back_end.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    @NotNull
    @Length(min = 2, max = 30)
    private String title;

    @NotNull
    @Length(min = 2, max = 255)
    private String description;

    @NotNull
    private LocalDate protectionStartDate;

    @NotNull
    private LocalDate protectionEndDate;

    @NotNull
    private LocalDate applicationEndDate;

    @NotNull
    @Length(min = 5, max = 100)
    private String contactInfo;

}
