package toyproject.syxxn.back_end.entity.review;

import org.junit.jupiter.api.Test;
import toyproject.syxxn.back_end.entity.GetAccount;
import toyproject.syxxn.back_end.entity.GetPost;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.application.Application;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReviewTest {

    private static final Account writer = GetAccount.account;
    private static final Account target = Account.builder()
            .email("yyuunn17@naver.com")
            .password("asdf1234")
            .nickname("야오히")
            .age(18)
            .sex(Sex.FEMALE)
            .isExperienceRaisingPet(false)
            .experienceDescription(null)
            .build();
    private static final Application application = Application.builder()
            .applicant(writer)
            .post(GetPost.post)
            .build();
    private static final Review review = Review.builder()
            .application(application)
            .target(target)
            .writer(writer)
            .grade(BigDecimal.ONE)
            .review("리뷰리뷰")
            .build();

    @Test
    void 계정_확인() {
        assertEquals(writer, review.getWriter());
        assertEquals(target, review.getTarget());
        assertTrue(writer.equals(application.getApplicant()) || target.equals(application.getApplicant()));
    }

    @Test
    void 업데이트() {
        review.update(BigDecimal.ZERO, "1점도 주기 아까워용");
        assertTrue(review.getIsUpdated());
    }

}
