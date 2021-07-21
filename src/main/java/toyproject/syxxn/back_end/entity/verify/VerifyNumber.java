package toyproject.syxxn.back_end.entity.verify;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(timeToLive = 60*3)
public class VerifyNumber {

    @Id
    private String email;

    private String verifyNumber;

    private boolean isVerified;

    public VerifyNumber isVerifiedTrue() {
        this.isVerified = true;
        return this;
    }

}
