package toyproject.syxxn.back_end.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentsResponse {

    private final List<CommentDto> comments;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CommentDto {

        private final Integer id;

        private final String nickname;

        private final String comment;

        private final String lastModifiedAt;

        private final Boolean isUpdated;

        @JsonProperty("is_mine")
        private final boolean isMine;

    }

}
