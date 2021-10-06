package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Integer id;

    private String nickname;

    private String comment;

    private String createdAt;

    private Boolean isUpdated;

    private String updatedAt;

}
