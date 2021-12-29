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
import toyproject.syxxn.back_end.exception.InvalidTokenException;
import toyproject.syxxn.back_end.exception.PasswordNotMatchedException;
import toyproject.syxxn.back_end.exception.UserNotAccessibleException;
import toyproject.syxxn.back_end.exception.UserNotFoundException;
import toyproject.syxxn.back_end.security.jwt.JwtTokenProvider;
import toyproject.syxxn.back_end.service.util.AuthenticationUtil;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder encoder;

    private final AuthenticationUtil authenticationFacade;

    public TokenResponse login(SignInRequest request) {
        Account account = getAccountNotBlocked(request.getEmail());

        if (!encoder.matches(request.getPassword(), account.getPassword())) {
            throw PasswordNotMatchedException.EXCEPTION;
        }

        return TokenResponse.builder()
                .refreshToken(jwtTokenProvider.getRefreshToken(account.getId()))
                .accessToken(jwtTokenProvider.getAccessToken(account.getId()))
                .build();
    }

    @Transactional
    public TokenResponse tokenRefresh(String receivedToken) {
        if(!jwtTokenProvider.isRefreshToken(receivedToken)) {
            throw InvalidTokenException.EXCEPTION;
        }
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(receivedToken)
                .map(token -> {
                    String refresh = jwtTokenProvider.generateRefreshToken(token.getAccountId());
                    token.update(refresh, token.getRefreshExp());
                    return token;
                })
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        return TokenResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .accessToken(jwtTokenProvider.getAccessToken(refreshToken.getAccountId()))
                .build();
    }

    public void logout() {
        Account account = accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(() -> UserNotAccessibleException.EXCEPTION);
        refreshTokenRepository.findById(account.getId())
                .ifPresent(refreshTokenRepository::delete);
    }

    private Account getAccountNotBlocked(String email) {
        return accountRepository.findByEmail(email)
                .filter(Account::isNotBlocked)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

}
