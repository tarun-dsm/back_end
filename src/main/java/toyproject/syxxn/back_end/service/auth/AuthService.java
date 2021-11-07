package toyproject.syxxn.back_end.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.syxxn.back_end.dto.request.SignInRequest;
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
import toyproject.syxxn.back_end.service.util.AuthenticationUtil;
import toyproject.syxxn.back_end.service.util.TokenUtil;
import toyproject.syxxn.back_end.service.util.UserUtil;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder encoder;

    private final UserUtil userUtil;
    private final TokenUtil tokenUtil;
    private final AuthenticationUtil authenticationFacade;

    public TokenResponse login(SignInRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .filter(user -> encoder.matches(request.getPassword(), user.getPassword()))
                .filter(userUtil::isNotBlocked)
                .orElseThrow(UserNotFoundException::new);

        if (account.getIsBlocked()) {
            throw new BlockedUserException();
        }

        return TokenResponse.builder()
                .refreshToken(tokenUtil.getRefreshToken(account.getId()))
                .accessToken(tokenUtil.getAccessToken(account.getId()))
                .build();
    }

    @Transactional
    public TokenResponse tokenRefresh(String receivedToken) {
        if(!jwtTokenProvider.isRefreshToken(receivedToken)) {
            throw new InvalidTokenException();
        }
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(receivedToken)
                .map(token -> {
                    String refresh = jwtTokenProvider.generateRefreshToken(token.getAccountId());
                    token.update(refresh, token.getRefreshExp());
                    return token;
                })
                .orElseThrow(UserNotFoundException::new);

        return TokenResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .accessToken(tokenUtil.getAccessToken(refreshToken.getAccountId()))
                .build();
    }

    public void logout() {
        Account user = accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotAccessibleException::new);
        try {
            refreshTokenRepository.deleteAllByAccountId(user.getId());
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }

}
