package toyproject.syxxn.back_end.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailsResponse {

    private Integer writerId;

    private String rating;

    private String nickname;

    @JsonProperty("is_mine")
    private boolean isMine;

    @JsonProperty("is_applied")
    private boolean isApplied;

    private PostDetailsDto post;

    private PetDetailsDto pet;

}
