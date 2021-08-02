package toyproject.syxxn.back_end.dto.response;

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

    private String nickname;

    private PostDetailsDto post;

    private PetDetailsDto pet;

}
