package toyproject.syxxn.back_end.service.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.dto.response.*;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.entity.review.Review;
import toyproject.syxxn.back_end.entity.review.ReviewRepository;
import toyproject.syxxn.back_end.exception.UserNotFoundException;
import toyproject.syxxn.back_end.exception.UserNotUnauthenticatedException;
import toyproject.syxxn.back_end.service.facade.AuthenticationFacade;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final AccountRepository accountRepository;
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;

    private final AuthenticationFacade authenticationFacade;

    @Override
    public ProfileResponse getProfile(Integer accountId) {
        Account account = getAccount(accountId);

        return ProfileResponse.builder()
                .nickname(account.getNickname())
                .age(account.getAge())
                .sex(account.getSex().getKorean())
                .avgGrade(account.getAvg())
                .rating(account.getRating())
                .administrationDivision(account.getAdministrationDivision())
                .isExperienceRasingPet(account.getIsExperienceRasingPet())
                .experience(account.getExperience() == null ? "": account.getExperience())
                .isLocationConfirm(account.getIsLocationConfirm())
                .build();
    }

    @Override
    public ProfileReviewResponse getReviews(Integer accountId) {
        Account account = getAccount(accountId);
        List<Review> reviews = reviewRepository.findAllByTarget(account);

        return new ProfileReviewResponse(reviews.stream().map(
                review -> ProfileReviewDto.builder()
                        .id(review.getId())
                        .nickname(review.getWriter().getNickname())
                        .grade(review.getGrade())
                        .comment(review.getComment())
                        .createdAt(review.getCreatedAt())
                        .build()
        ).collect(Collectors.toList()));
    }

    @Override
    public ProfilePostResponse getPosts(Integer accountId) {
        Account account = getAccount(accountId);
        List<Post> posts = postRepository.findAllByAccountAndPetImagesNotNullOrderByCreatedAtDesc(account);

        return new ProfilePostResponse(posts.stream().map(
                post ->
                    ProfilePostDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .firstImagePath(post.getPetImages().get(0).getPath())
                            .createdAt(post.getCreatedAt())
                            .isApplicationEnd(post.getIsApplicationEnd())
                            .build()

        ).collect(Collectors.toList()));
    }

    private Account getAccount(Integer accountId) {
        if (accountId == null) {
            return accountRepository.findByEmail(authenticationFacade.getUserEmail())
                    .orElseThrow(UserNotUnauthenticatedException::new);
        } else {
            return accountRepository.findById(accountId)
                    .orElseThrow(UserNotFoundException::new);
        }
    }

}
