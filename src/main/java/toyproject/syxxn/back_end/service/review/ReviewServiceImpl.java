package toyproject.syxxn.back_end.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.dto.request.ReviewRequest;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.review.Review;
import toyproject.syxxn.back_end.entity.review.ReviewRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.security.auth.AuthenticationFacade;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final AccountRepository accountRepository;
    private final ApplicationRepository applicationRepository;
    private final ReviewRepository reviewRepository;

    private final AuthenticationFacade authenticationFacade;

    @Override
    public void writeReview(Integer applicationId, ReviewRequest request) {
        Account writer = getAccount();
        Application application = applicationRepository.findById(applicationId)
                .filter(Application::getIsAccepted)
                .orElseThrow(ApplicationNotFoundException::new);

        reviewRepository.findByWriterAndApplication(writer, application)
                .ifPresent(review -> {
                    throw new UserAlreadyWrittenReviewException();
                });

        if (!isApplicationClosed(application.getPost())) {
            throw new NotReviewablePeriodException();
        }

        Account target;
        if (writer.getId().equals(application.getAccount().getId())) {
            target = application.getPost().getAccount();
        } else if(writer.getId().equals(application.getPost().getAccount().getId())) {
            target = application.getAccount();
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

    @Override
    public void deleteReview(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .filter(r -> r.getWriter().getId().equals(getAccount().getId()))
                .orElseThrow(ReviewNotFoundException::new);

        reviewRepository.delete(review);
    }

    @Override
    public void updateReview(Integer reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .filter(r -> r.getWriter().getId().equals(getAccount().getId()))
                .orElseThrow(ReviewNotFoundException::new);

        review.update(request.getGrade(), request.getComment());

        reviewRepository.save(review);
    }

    private Account getAccount() {
        return accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .filter(Account::getIsLocationConfirm)
                .orElseThrow(UserNotUnauthenticatedException::new);
    }

    private boolean isApplicationClosed(Post post) {
        return (LocalDate.now().isAfter(post.getApplicationEndDate()) || post.getIsEnd());
    }

}
