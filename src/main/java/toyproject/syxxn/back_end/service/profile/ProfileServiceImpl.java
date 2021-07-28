package toyproject.syxxn.back_end.service.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.dto.response.ProfileResponse;
import toyproject.syxxn.back_end.dto.response.ProfileReviewDto;
import toyproject.syxxn.back_end.dto.response.ProfileReviewResponse;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.review.Review;
import toyproject.syxxn.back_end.entity.review.ReviewRepository;
import toyproject.syxxn.back_end.exception.UserNotFoundException;
import toyproject.syxxn.back_end.security.auth.AuthenticationFacade;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final AccountRepository accountRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ProfileResponse getProfile(Integer accountId) {
        Account account = getAccount(accountId);
        List<Review> reviews = reviewRepository.findAllByTarget(account);
        BigDecimal avgGrade = getAvg(reviews);

        String experience = account.getExperience() == null ? "": account.getExperience();
        String sex = account.getSex().equals(Sex.MALE)?"남성":"여성";

        return ProfileResponse.builder()
                .nickname(account.getNickname())
                .age(account.getAge())
                .sex(sex)
                .avgGrade(avgGrade)
                .rating(getRating(avgGrade.doubleValue()))
                .isExperienceRasingPet(account.getIsExperienceRasingPet())
                .experience(experience)
                .isLocationConfirm(account.getIsLocationConfirm())
                .build();
    }

    @Override
    public ProfileReviewResponse getReviews(Integer accountId) {
        Account account = getAccount(accountId);
        List<Review> reviews = reviewRepository.findAllByTarget(account);
        List<ProfileReviewDto> reviewDto = new ArrayList<>();

        for (Review review : reviews) {
            reviewDto.add(
                    ProfileReviewDto.builder()
                            .id(review.getId())
                            .nickName(review.getWriter().getNickname())
                            .grade(review.getGrade())
                            .comment(review.getComment())
                            .createdAt(review.getCreatedAt())
                            .build()
            );
        }

        return new ProfileReviewResponse(reviewDto);
    }

    private Account getAccount(Integer accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(UserNotFoundException::new);
    }

    private static BigDecimal getAvg(List<Review> reviews) {
        BigDecimal sumData = BigDecimal.ZERO;
        for (int i = 0; i < reviews.size(); i++){
            sumData = sumData.add(reviews.get(i).getGrade());
        }
        return sumData.divide(BigDecimal.valueOf(reviews.size()), MathContext.DECIMAL64);
    }

    private static String getRating(Double avgGrade) {
        if (avgGrade.compareTo(4.5) > 0) {
            return "1등급";
        } else if (avgGrade.compareTo(3.5) > 0) {
            return "2등급";
        } else if (avgGrade.compareTo(2.5) > 0) {
            return "3등급";
        } else if (avgGrade.compareTo(1.5) > 0) {
            return "4등급";
        }
        return "5등급";
    }

}
