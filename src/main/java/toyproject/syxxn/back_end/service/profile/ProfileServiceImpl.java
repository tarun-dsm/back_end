package toyproject.syxxn.back_end.service.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.dto.response.ProfileResponse;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.review.Review;
import toyproject.syxxn.back_end.entity.review.ReviewRepository;
import toyproject.syxxn.back_end.exception.UserNotFoundException;
import toyproject.syxxn.back_end.security.auth.AuthenticationFacade;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final AccountRepository accountRepository;
    private final ReviewRepository reviewRepository;

    private final AuthenticationFacade authenticationFacade;

    @Override
    public ProfileResponse getProfile(Integer accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(UserNotFoundException::new);
        List<Review> reviews = reviewRepository.findAllByTarget(account);
        BigDecimal avgGrade = getAvg(reviews);

        boolean isMyself = false;

        if (account.getEmail().equals(authenticationFacade.getUserEmail())) {
            isMyself = true;
        }

        return ProfileResponse.builder()
                .isMyself(isMyself)
                .nickname(account.getNickname())
                .age(account.getAge())
                .sex(account.getSex().toString())
                .avgGrade(avgGrade)
                .rating(getRating(avgGrade.doubleValue()))
                .isExperienceRasingPet(account.getIsExperienceRasingPet())
                .experience(account.getExperience())
                .isLocationConfirm(account.getIsLocationConfirm())
                .build();
    }

    private static BigDecimal getAvg(List<Review> reviews) {
        BigDecimal sumData = BigDecimal.ZERO;
        for (int i = 0; i < reviews.size(); i++){
            sumData = sumData.add(reviews.get(i).getGrade());
        }
        return sumData.divide(BigDecimal.valueOf(reviews.size()), MathContext.DECIMAL64);
    }

    private static String getRating(Double avgGrade) {
        if (avgGrade.compareTo(5.0) > 0) {
            return "1등급";
        } else if (avgGrade.compareTo(4.0) > 0) {
            return "2등급";
        } else if (avgGrade.compareTo(3.0) > 0) {
            return "3등급";
        } else if (avgGrade.compareTo(2.0) > 0) {
            return "4등급";
        }
        return "5등급";
    }

}
