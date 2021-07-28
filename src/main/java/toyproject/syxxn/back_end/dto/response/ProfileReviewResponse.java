package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProfileReviewResponse {

    List<ProfileReviewDto> reviews;

}
