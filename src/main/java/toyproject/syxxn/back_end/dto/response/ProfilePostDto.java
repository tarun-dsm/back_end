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
public class ProfilePostDto {

    private Integer id;

    private String title;

    private String firstImagePath;

    private LocalDateTime createdAt;

    private Boolean isApplicationEnd;

}