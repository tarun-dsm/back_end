package toyproject.syxxn.back_end.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.dto.request.SignUpRequest;
import toyproject.syxxn.back_end.dto.response.TokenResponse;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.account.Sex;
import toyproject.syxxn.back_end.exception.UserEmailAlreadyExistsException;
import toyproject.syxxn.back_end.exception.UserNicknameAlreadyExistsException;
import toyproject.syxxn.back_end.security.jwt.JwtTokenProvider;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;

    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenResponse signUp(SignUpRequest request) {
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

        return TokenResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(account.getId()))
                .refreshToken(jwtTokenProvider.generateRefreshToken(account.getId()))
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
