package toyproject.syxxn.back_end.entity.refresh_token;

import org.junit.jupiter.api.Test;
import toyproject.syxxn.back_end.entity.GetAccount;
import toyproject.syxxn.back_end.entity.account.Account;

import static org.junit.jupiter.api.Assertions.*;

public class RefreshTokenTest {

    private static final long refreshExp = 1234L;

    private static final Account account = GetAccount.account;
    private static final RefreshToken refreshToken = RefreshToken.builder()
            .accountId(account.getId())
            .refreshExp(refreshExp)
            .refreshToken("asdf.asdf.asdf")
            .build();

    @Test
    void 널_여부_확인() {
        assertNotNull(refreshToken.getRefreshToken());
        assertNull(refreshToken.getAccountId()); // IDENTITY 방식은 데이터베이스에 권한을 넘긴다. 데이터베이스에 Insert를 했을 때 AI가 생성된다.
    }

    @Test
    void 업데이트() {
        refreshToken.update("qwer.qwer.qwer", refreshExp);
        assertEquals("qwer.qwer.qwer", refreshToken.getRefreshToken());
    }

}
