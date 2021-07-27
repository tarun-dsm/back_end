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
public class MyApplicationDto {

    private Integer id;

    private String postName;

    private Integer postId;

    private boolean isAccepted;

    private boolean isEnd;

    private LocalDateTime applicationDate;

}
