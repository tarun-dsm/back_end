package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Integer id;

    private String title;

    private String administrationDivision;

    private String firstImagePath;

    private LocalDate protectionStartDate;

    private LocalDate protectionEndDate;

}
