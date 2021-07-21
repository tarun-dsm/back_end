package toyproject.syxxn.back_end.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.dto.request.ReviewRequest;
import toyproject.syxxn.back_end.dto.request.UpdateReviewRequest;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.review.Review;
import toyproject.syxxn.back_end.entity.review.ReviewRepository;
import toyproject.syxxn.back_end.exception.UserNotAccessibleException;
import toyproject.syxxn.back_end.exception.ReviewNotFoundException;
import toyproject.syxxn.back_end.exception.UserNotFoundException;
import toyproject.syxxn.back_end.security.auth.AuthenticationFacade;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final AccountRepository accountRepository;
    private final ReviewRepository reviewRepository;

    private final AuthenticationFacade authenticationFacade;


    @Override
    @Transactional
    public void writeReview(ReviewRequest request) {
        isLogin();
        Account writer = accountRepository.findById(authenticationFacade.getUserId())
                .orElseThrow(UserNotFoundException::new);
        Account target = accountRepository.findById(request.getTargetId())
                .orElseThrow(UserNotFoundException::new);

        reviewRepository.save(
                Review.builder()
                        .comment(request.getComment())
                        .ratingScore(request.getRatingScore())
                        .writer(writer)
                        .target(target)
                        .build()
        );
    }

    @Override
    public void deleteReview(Long id) {
        isLogin();

        Review review = reviewRepository.findById(id)
                .orElseThrow(ReviewNotFoundException::new);
        if (review.getWriter().getId().equals(authenticationFacade.getUserId())) {
            throw new UserNotAccessibleException();
        }

        reviewRepository.delete(review);
    }

    @Override
    @Transactional
    public void updateReview(Long id, UpdateReviewRequest request) {
        isLogin();

        Review review = reviewRepository.findById(id)
                .orElseThrow(ReviewNotFoundException::new);
        if (review.getWriter().getId().equals(authenticationFacade.getUserId())) {
            throw new UserNotAccessibleException();
        }

        review.update(review.getComment(), review.getRatingScore());
        reviewRepository.save(review);
    }

    private void isLogin() {
        if (authenticationFacade.getUserId() == null) {
            throw new UserNotAccessibleException();
        }
    }

}
