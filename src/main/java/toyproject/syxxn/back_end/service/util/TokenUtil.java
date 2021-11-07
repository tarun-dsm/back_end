package toyproject.syxxn.back_end.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshToken;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshTokenRepository;
import toyproject.syxxn.back_end.security.jwt.JwtTokenProvider;

@RequiredArgsConstructor
@Component
public class TokenUtil {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${auth.jwt.exp.refresh}")
    private Long refreshExp;

    public String getAccessToken(Integer accountId) {
        return jwtTokenProvider.generateAccessToken(accountId);
    }

    public String getRefreshToken(Integer accountId) {
        return refreshTokenRepository.save(
                RefreshToken.builder()
                        .accountId(accountId)
                        .refreshExp(refreshExp)
                        .refreshToken(jwtTokenProvider.generateRefreshToken(accountId))
                        .build()
        ).getRefreshToken();
    }

}
