package toyproject.syxxn.back_end.service.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.dto.response.*;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.entity.review.Review;
import toyproject.syxxn.back_end.entity.review.ReviewRepository;
import toyproject.syxxn.back_end.exception.BlockedUserException;
import toyproject.syxxn.back_end.exception.UserNotAccessibleException;
import toyproject.syxxn.back_end.exception.UserNotFoundException;
import toyproject.syxxn.back_end.exception.UserNotUnauthenticatedException;
import toyproject.syxxn.back_end.service.util.AuthenticationUtil;
import toyproject.syxxn.back_end.service.util.S3Util;
import toyproject.syxxn.back_end.service.util.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final AccountRepository accountRepository;
    private final ApplicationRepository applicationRepository;
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;

    private final S3Util s3Util;
    private final AuthenticationUtil authenticationUtil;

    public ProfileResponse getProfile(Integer accountId) {
        Account account = getAccount(accountId);

        return ProfileResponse.builder()
                .nickname(account.getNickname())
                .age(account.getAge())
                .sex(account.getSex().getKorean())
                .avgGrade(account.getAvg())
                .rating(account.getRating())
                .administrationDivision(account.getAdministrationDivision())
                .isExperienceRaisingPet(account.getIsExperienceRaisingPet())
                .experience(account.getExperience() == null ? "" : account.getExperience())
                .isLocationConfirm(account.getIsLocationConfirm())
                .build();
    }

    public ProfileReviewResponse getReviews(Integer accountId) {
        Account account = getMe();
        Account accessedAccount = getAccount(accountId);
        if (!account.getIsLocationConfirm()) {
            return new ProfileReviewResponse(new ArrayList<>());
        }

        return new ProfileReviewResponse(reviewRepository.findAllByTarget(accessedAccount).stream().map(
                review -> ProfileReviewResponse.ProfileReviewDto.builder()
                        .id(review.getId())
                        .nickname(review.getWriter().getNickname())
                        .grade(review.getGrade())
                        .comment(review.getComment())
                        .createdAt(review.getCreatedAtToLocalDate())
                        .isMyReview(review.getWriter().equals(account))
                        .build()
        ).collect(Collectors.toList()));
    }

    public ProfilePostResponse getPosts(Integer accountId) {
        Account account = getMe();
        Account accessedAccount = getAccount(accountId);
        if (!account.getIsLocationConfirm()) {
            return new ProfilePostResponse(new ArrayList<>());
        }

        return new ProfilePostResponse(postRepository.findAllByAccountAndPetImagesNotNullOrderByCreatedAtDesc(accessedAccount).stream().map(
                post -> {
                    Optional<Application> application = applicationRepository.findByPostAndIsAcceptedTrue(post);
                    return ProfilePostResponse.ProfilePostDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .firstImagePath(s3Util.getS3ObjectUrl(post.getPetImages().get(0).getPath()))
                            .createdAt(post.getCreatedAtToString())
                            .isApplicationEnd(post.getIsApplicationEnd())
                            .protectorId(application.isEmpty() ? null : application.get().getApplicant().getId().toString())
                            .protectorNickname(application.isEmpty() ? null : application.get().getApplicant().getNickname())
                            .build();
                }
        ).collect(Collectors.toList()));
    }

    private Account getAccount(Integer accountId) {
        if (accountId == null) {
            return getMe();
        } else {
            if (!getMe().getIsLocationConfirm()) {
                throw new UserNotAccessibleException();
            }

            return accountRepository.findById(accountId)
                    .map(this::isBlocked)
                    .orElseThrow(UserNotFoundException::new);
        }
    }

    private Account getMe() {
        return accountRepository.findByEmail(authenticationUtil.getUserEmail())
                .map(this::isBlocked)
                .orElseThrow(UserNotUnauthenticatedException::new);
    }

    private Account isBlocked(Account account) {
        if (account.getIsBlocked()) {
            throw new BlockedUserException();
        }
        return account;
    }

}
