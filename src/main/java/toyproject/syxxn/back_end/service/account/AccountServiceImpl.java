package toyproject.syxxn.back_end.service.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.syxxn.back_end.dto.request.CoordinatesRequest;
import toyproject.syxxn.back_end.dto.request.SignUpRequest;
import toyproject.syxxn.back_end.dto.response.TokenResponse;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshToken;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshTokenRepository;
import toyproject.syxxn.back_end.entity.verify.VerifyNumber;
import toyproject.syxxn.back_end.entity.verify.VerifyNumberRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.service.facade.AuthenticationFacade;
import toyproject.syxxn.back_end.security.jwt.JwtTokenProvider;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerifyNumberRepository verifyNumberRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationFacade authenticationFacade;

    private final PasswordEncoder encoder;

    @Value("${auth.jwt.exp.refresh}")
    private Long refreshExp;

    @Value("${kakao.api-key}")
    private String restApiKey;

    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=";

    @Transactional
    @Override
    public TokenResponse signUp(SignUpRequest request) {
        verifyNumberRepository.findById(request.getEmail())
                .filter(VerifyNumber::isVerified)
                .orElseThrow(UserNotUnauthenticatedException::new);

        Account account = accountRepository.save(
                Account.builder()
                        .email(request.getEmail())
                        .password(encoder.encode(request.getPassword()))
                        .nickname(request.getNickname())
                        .age(request.getAge())
                        .sex(Sex.valueOf(request.getSex()))
                        .isExperienceRaisingPet(request.isExperienceRasingPet())
                        .experience(request.getExperience())
                        .isLocationConfirm(false)
                        .latitude(BigDecimal.ZERO)
                        .longitude(BigDecimal.ZERO)
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

    @Override
    public void saveCoordinate(CoordinatesRequest request) throws JsonProcessingException, UnirestException {
        Account account = accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);
        BigDecimal x = request.getLongitude();
        BigDecimal y = request.getLatitude();

        System.out.println(getAdministrationDivision(x.doubleValue(), y.doubleValue()));
        account.updateLocation(request.getLongitude(), request.getLatitude(), getAdministrationDivision(x.doubleValue(),y.doubleValue()));
        accountRepository.save(account);
    }

    private String getAdministrationDivision(Double x, Double y) throws JsonProcessingException, UnirestException {
        ObjectMapper mapper = new ObjectMapper();
        String administrationDivision = "";

        HttpResponse<com.mashape.unirest.http.JsonNode> response = Unirest.get(KAKAO_API_URL + x + "&y=" + y)
                .header("Authorization", "KakaoAK " + restApiKey)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8")
                .asJson();

        String json = response.getBody().toString();

        for (int i = 0; i < 1; i++) {
            JsonNode document = mapper.readTree(json).path("documents").get(i);

            if (!document.path("region_4depth_name").toString().equals("\"\"")) {
                administrationDivision = document.path("region_4depth_name").toString();
                break;
            } else {
                administrationDivision = document.path("region_3depth_name").toString();
            }
        }

        return administrationDivision;
    }

}
