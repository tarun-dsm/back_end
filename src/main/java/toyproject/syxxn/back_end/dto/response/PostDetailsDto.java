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
public class PostDetailsDto {

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
