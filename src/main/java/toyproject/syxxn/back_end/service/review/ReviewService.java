package toyproject.syxxn.back_end.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.dto.request.ReviewRequest;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.review.Review;
import toyproject.syxxn.back_end.entity.review.ReviewRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.service.util.UserUtil;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ApplicationRepository applicationRepository;
    private final ReviewRepository reviewRepository;

    private final UserUtil userUtil;

    public void writeReview(Integer applicationId, ReviewRequest request) {
        Account me = userUtil.getLocalConfirmAccount();
        Application application = applicationRepository.findById(applicationId)
                .filter(Application::getIsAccepted)
                .orElseThrow(() -> ApplicationNotFoundException.EXCEPTION);

        reviewRepository.findByWriterAndApplication(me, application)
                .ifPresent(review -> {
                    throw UserAlreadyWrittenReviewException.EXCEPTION;
                });

        if (!isReviewablePeriod(application.getPost())) {
            throw NotReviewablePeriodException.EXCEPTION;
        }

        Account target;
        if (me.equals(application.getApplicant())) {
            target = application.getPost().getAccount();
        } else if (me.equals(application.getPost().getAccount())) {
            target = application.getApplicant();
        } else {
            throw UserNotAccessibleException.EXCEPTION;
        }

        reviewRepository.save(
                Review.builder()
                        .application(application)
                        .target(target)
                        .writer(me)
                        .grade(request.getGrade())
                        .review(request.getReview())
                        .build()
        );
    }

    public void deleteReview(Integer reviewId) {
        Review review = getReview(reviewId);
        reviewRepository.delete(review);
    }

    public void updateReview(Integer reviewId, ReviewRequest request) {
        Review review = getReview(reviewId);
        review.update(request.getGrade(), request.getReview());

        reviewRepository.save(review);
    }

    private boolean isReviewablePeriod(Post post) {
        return (LocalDate.now().isAfter(post.getApplicationEndDate()) && post.getIsApplicationEnd());
    }

    private Review getReview(Integer reviewId) {
        Review review =  reviewRepository.findById(reviewId)
                .orElseThrow(() -> ReviewNotFoundException.EXCEPTION);
        if(!review.getWriter().equals(userUtil.getLocalConfirmAccount()))
            throw UserNotAccessibleException.EXCEPTION;

        return review;
    }

}
