package toyproject.syxxn.back_end.entity.refreshtoken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash
public class RefreshToken implements Serializable {

    @Id
    private Integer accountId;

    @Indexed
    private String refreshToken;

    @TimeToLive
    private Long refreshExp;

    public RefreshToken update(Long refreshExp) {
        this.refreshExp = refreshExp;
        return this;
    }

}
