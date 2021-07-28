package toyproject.syxxn.back_end.service.review;

import toyproject.syxxn.back_end.dto.request.ReviewRequest;

public interface ReviewService {
    void writeReview(Integer applicationId, ReviewRequest request);
    void deleteReview(Integer reviewId);
    void updateReview(Integer reviewId, ReviewRequest request);
}
