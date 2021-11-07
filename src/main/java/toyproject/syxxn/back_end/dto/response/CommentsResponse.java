package toyproject.syxxn.back_end.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsResponse {

    private List<CommentDto> comments;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentDto {

        private Integer id;

        private String nickname;

        private String comment;

        private String createdAt;

        private Boolean isUpdated;

        private String updatedAt;

        @JsonProperty("is_mine")
        private boolean isMine;

    }

}
