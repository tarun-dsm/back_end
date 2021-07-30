package toyproject.syxxn.back_end.service.profile;

import toyproject.syxxn.back_end.dto.response.PostResponse;
import toyproject.syxxn.back_end.dto.response.ProfileResponse;
import toyproject.syxxn.back_end.dto.response.ProfileReviewResponse;

public interface ProfileService {
    ProfileResponse getProfile(Integer accountId);
    ProfileReviewResponse getReviews(Integer accountId);
    PostResponse getPosts(Integer accountId);
}