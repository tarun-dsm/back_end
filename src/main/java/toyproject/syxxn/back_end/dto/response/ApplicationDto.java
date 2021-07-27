package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {

    private Integer applicationId;

    private Integer applicantId;

    private String applicantNickname;

    private LocalDateTime applicationDate;

}
