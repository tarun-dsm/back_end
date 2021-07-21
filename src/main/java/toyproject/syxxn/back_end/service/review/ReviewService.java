package toyproject.syxxn.back_end.service.review;

import toyproject.syxxn.back_end.dto.request.ReviewRequest;
import toyproject.syxxn.back_end.dto.request.UpdateReviewRequest;

public interface ReviewService {
    void writeReview(ReviewRequest request);
    void deleteReview(Long id);
    void updateReview(Long id, UpdateReviewRequest request);
}
