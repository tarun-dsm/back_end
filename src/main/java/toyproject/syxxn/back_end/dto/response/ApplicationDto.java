package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {

    private Integer applicationId;

    private Integer applicantId;

    private String applicantNickname;

    @DateTimeFormat(pattern = "yyyy-MM-dd`T`hh:mm:ss")
    private LocalDateTime applicationDate;

}
