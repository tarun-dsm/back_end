package toyproject.syxxn.back_end.entity.verify_number;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(timeToLive = 60*4)
public class VerifyNumber {

    @Id
    private String email;

    private String verifyNumber;

    private boolean isVerified;

    public VerifyNumber(String email, String verifyNumber) {
        this.email = email;
        this.verifyNumber = verifyNumber;
        this.isVerified = false;
    }

    public VerifyNumber updateVerifyStatus() {
        this.isVerified = true;
        return this;
    }

}
