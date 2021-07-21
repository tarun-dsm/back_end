package toyproject.syxxn.back_end.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.syxxn.back_end.dto.request.SignUpRequest;
import toyproject.syxxn.back_end.dto.response.TokenResponse;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshToken;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshTokenRepository;
import toyproject.syxxn.back_end.entity.verify.VerifyNumberRepository;
import toyproject.syxxn.back_end.exception.UserEmailAlreadyExistsException;
import toyproject.syxxn.back_end.exception.UserNicknameAlreadyExistsException;
import toyproject.syxxn.back_end.exception.UserNotUnauthenticatedException;
import toyproject.syxxn.back_end.security.jwt.JwtTokenProvider;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerifyNumberRepository verifyNumberRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder encoder;

    @Value("${auth.jwt.exp.refresh}")
    private Long refreshExp;

    @Transactional
    @Override
    public TokenResponse signUp(SignUpRequest request) {
        verifyNumberRepository.findById(request.getEmail())
                .orElseThrow(UserNotUnauthenticatedException::new);

        Account account = accountRepository.save(
                Account.builder()
                        .email(request.getEmail())
                        .password(encoder.encode(request.getPassword()))
                        .nickname(request.getNickname())
                        .age(request.getAge())
                        .sex(Sex.valueOf(request.getSex()))
                        .isExperienceRasingPet(request.isExperienceRasingPet())
                        .experience(request.getExperience())
                        .address(request.getAddress())
                        .isLocationConfirm(false)
                        .build()
        );

        RefreshToken refreshToken = refreshTokenRepository.save(
                RefreshToken.builder()
                        .accountId(account.getId())
                        .refreshExp(refreshExp)
                        .refreshToken(jwtTokenProvider.generateRefreshToken(account.getId()))
                        .build()
        );

        return TokenResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(account.getId()))
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    @Override
    public void confirmEmail(String email) {
        accountRepository.findByEmail(email)
                .ifPresent(account -> {throw new UserEmailAlreadyExistsException(); });
    }

    @Override
    public void confirmNickname(String nickname) {
        accountRepository.findByNickname(nickname)
                .ifPresent(account -> {throw new UserNicknameAlreadyExistsException(); });
    }

}
