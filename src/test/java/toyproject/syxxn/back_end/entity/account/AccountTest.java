package toyproject.syxxn.back_end.entity.account;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import toyproject.syxxn.back_end.entity.Sex;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountTest {

    private static final String email = "test@naver.com";
    private static final String password = "password";

    private static final Account account = Account.builder()
            .email(email)
            .password(password)
            .nickname("꼬순내친구들")
            .age(20)
            .sex(Sex.FEMALE)
            .isExperienceRaisingPet(false)
            .experienceDescription(null)
            .build();

    @Test
    void 유저_이메일_비밀번호_일치() {
        assertEquals(email, account.getEmail());
        assertEquals(password, account.getPassword());
    }

    @Test
    void 유저_객체_생성() {
        Account account = new Account();
        assertNull(account.getEmail());
        assertNull(account.getPassword());
        assertNull(account.getNickname());
    }

    @Test
    void 유저_차단_여부() {
        assertFalse(account.getIsBlocked());
    }

    @Order(0)
    @Test
    void 유저_위치_확인() {
        assertEquals(BigDecimal.ZERO, account.getLongitude());
        assertEquals(BigDecimal.ZERO, account.getLatitude());
    }

    @Test
    void 유저_위치_수정() {
        account.updateLocation(BigDecimal.valueOf(123.123), BigDecimal.valueOf(123.123), "꼬친동");
        assertTrue(account.getIsLocationConfirm());
        assertEquals(BigDecimal.valueOf(123.123), account.getLongitude());
        assertEquals(BigDecimal.valueOf(123.123), account.getLatitude());
    }

    @Test
    void 유저_평균_점수() {
        assertEquals("처음처럼", account.getRating().getRating());
    }

    @Test
    void 경험글_널여부() {
        assertNull(account.getExperienceDescription());
    }

}
