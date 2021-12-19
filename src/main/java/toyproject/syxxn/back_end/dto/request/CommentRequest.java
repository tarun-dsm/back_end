package toyproject.syxxn.back_end.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequest {

    @Size(max = 100, message = "댓글을 100자 이내로 작성해 주세요.")
    @NotBlank
    private String comment;

}
