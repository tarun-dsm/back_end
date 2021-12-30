package toyproject.syxxn.back_end.entity.refresh_token;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash
public class RefreshToken implements Serializable {

    @Id
    private Integer accountId;

    @Indexed
    private String refreshToken;

    @TimeToLive
    private long refreshExp;

    public void update(String refreshToken, long refreshExp) {
        this.refreshToken = refreshToken;
        this.refreshExp = refreshExp;
    }

}
