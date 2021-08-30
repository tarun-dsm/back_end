package toyproject.syxxn.back_end.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.dto.request.SignInRequest;
import toyproject.syxxn.back_end.dto.response.AccessTokenResponse;
import toyproject.syxxn.back_end.dto.response.TokenResponse;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshToken;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshTokenRepository;
import toyproject.syxxn.back_end.exception.BlockedUserException;
import toyproject.syxxn.back_end.exception.InvalidTokenException;
import toyproject.syxxn.back_end.exception.UserNotAccessibleException;
import toyproject.syxxn.back_end.exception.UserNotFoundException;
import toyproject.syxxn.back_end.security.jwt.JwtTokenProvider;
import toyproject.syxxn.back_end.service.facade.BaseService;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder encoder;

    @Value("${auth.jwt.exp.refresh}")
    private Long refreshExp;

    private final BaseService baseService;

    @Override
    public TokenResponse login(SignInRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .filter(user -> encoder.matches(request.getPassword(), user.getPassword()))
                .map(baseService::isBlocked)
                .orElseThrow(UserNotFoundException::new);

        if (account.getIsBlocked()) {
            throw new BlockedUserException();
        }

        RefreshToken refreshToken = refreshTokenRepository.save(
                RefreshToken.builder()
                        .accountId(account.getId())
                        .refreshExp(refreshExp)
                        .refreshToken(jwtTokenProvider.generateRefreshToken(account.getId()))
                        .build()
        );

        return TokenResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .accessToken(jwtTokenProvider.generateAccessToken(account.getId()))
                .build();
    }

    @Override
    public AccessTokenResponse tokenRefresh(String receivedToken) {
        if(!jwtTokenProvider.isRefreshToken(receivedToken)) {
            throw new InvalidTokenException();
        }
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(receivedToken)
                .map(refreshToken1 -> refreshToken1.update(refreshExp))
                .orElseThrow(UserNotFoundException::new);
        refreshTokenRepository.save(refreshToken);

        return new AccessTokenResponse(jwtTokenProvider.generateAccessToken(refreshToken.getAccountId()));
    }

}
