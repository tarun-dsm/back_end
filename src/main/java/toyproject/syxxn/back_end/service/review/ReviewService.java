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
        Account writer = userUtil.getLocalConfirmAccount();
        Application application = applicationRepository.findById(applicationId)
                .filter(Application::getIsAccepted)
                .orElseThrow(() -> ApplicationNotFoundException.EXCEPTION);

        reviewRepository.findByWriterAndApplication(writer, application)
                .ifPresent(review -> {
                    throw new UserAlreadyWrittenReviewException();
                });

        if (!isReviewablePeriod(application.getPost())) {
            throw new NotReviewablePeriodException();
        }

        Account target;
        if (writer.getId().equals(application.getApplicant().getId())) {
            target = application.getPost().getAccount();
        } else if (writer.getEmail().equals(application.getPost().getAccount().getEmail())) {
            target = application.getApplicant();
        } else {
            throw new UserNotAccessibleException();
        }

        reviewRepository.save(
                Review.builder()
                        .application(application)
                        .target(target)
                        .writer(writer)
                        .grade(request.getGrade())
                        .comment(request.getComment())
                        .build()
        );
    }

    public void deleteReview(Integer reviewId) {
        Review review = getReview(reviewId);
        reviewRepository.delete(review);
    }

    public void updateReview(Integer reviewId, ReviewRequest request) {
        Review review = getReview(reviewId);
        review.update(request.getGrade(), request.getComment());

        reviewRepository.save(review);
    }

    private boolean isReviewablePeriod(Post post) {
        return (LocalDate.now().isAfter(post.getApplicationEndDate()) && post.getIsApplicationEnd());
    }

    private Review getReview(Integer reviewId) {
        return reviewRepository.findById(reviewId)
                .filter(r -> r.getWriter().equals(userUtil.getLocalConfirmAccount()))
                .orElseThrow(ReviewNotFoundException::new);
    }

}
