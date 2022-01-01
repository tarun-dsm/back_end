package toyproject.syxxn.back_end.entity.report;

import org.junit.jupiter.api.Test;
import toyproject.syxxn.back_end.entity.GetAccount;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.account.Account;

import static org.junit.jupiter.api.Assertions.*;

public class ReportTest {

    private static final Account reporter = GetAccount.account;
    private static final Account target = Account.builder()
            .email("yyuunn17@naver.com")
            .password("asdf1234")
            .nickname("야오히")
            .age(18)
            .sex(Sex.FEMALE)
            .isExperienceRaisingPet(false)
            .experienceDescription(null)
            .build();
    private static final Report report = Report.builder()
            .reason("저흐 고양이 츄르를 안 챙겨주셨어요ㅜㅜ")
            .target(target)
            .reporter(reporter)
            .build();

    @Test
    void 생성일_확인하기() {
        assertNull(report.getLastModifiedAt()); // @LastModifiedDate는 JPA와 관련된 어노테이션이기 때문에 데이터베이스에 저장할 때 생성된다.
    }

    @Test
    void 신고자_대상자_확인() {
        assertEquals(target, report.getTarget());
        assertEquals(reporter, report.getReporter());
    }

}
