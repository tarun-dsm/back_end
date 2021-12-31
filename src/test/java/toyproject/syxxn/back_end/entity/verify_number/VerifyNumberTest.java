package toyproject.syxxn.back_end.entity.verify_number;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import toyproject.syxxn.back_end.entity.GetAccount;
import toyproject.syxxn.back_end.entity.account.Account;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VerifyNumberTest {

    private static final Account account = GetAccount.account;
    private static final VerifyNumber verifyNumber = new VerifyNumber(
            account.getEmail(),
            "123456"
    );

    @Order(0)
    @Test
    void 인증여부() {
        assertFalse(verifyNumber.isVerified());
    }

    @Test
    void 인증() {
        verifyNumber.updateVerifyStatus();
        assertTrue(verifyNumber.isVerified());
    }

}
