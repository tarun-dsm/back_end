package toyproject.syxxn.back_end.service.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.syxxn.back_end.dto.request.CoordinatesRequest;
import toyproject.syxxn.back_end.dto.request.SignUpRequest;
import toyproject.syxxn.back_end.dto.response.HaveEverBeenEntrustedResponse;
import toyproject.syxxn.back_end.dto.response.TokenResponse;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.report.Report;
import toyproject.syxxn.back_end.entity.report.ReportRepository;
import toyproject.syxxn.back_end.entity.verify.VerifyNumber;
import toyproject.syxxn.back_end.entity.verify.VerifyNumberRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.security.jwt.JwtTokenProvider;
import toyproject.syxxn.back_end.service.util.AuthenticationUtil;
import toyproject.syxxn.back_end.service.util.UserUtil;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ApplicationRepository applicationRepository;
    private final ReportRepository reportRepository;
    private final VerifyNumberRepository verifyNumberRepository;

    private final AuthenticationUtil authenticationUtil;
    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder encoder;

    @Value("${kakao.api-key}")
    private String restApiKey;

    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?";

    private final UserUtil userUtil;

    @Transactional
    public TokenResponse signUp(SignUpRequest request) {
        verifyNumberRepository.findById(request.getEmail())
                .filter(VerifyNumber::isVerified)
                .orElseThrow(() -> UserNotAuthenticatedException.EXCEPTION);

        if (!request.getIsExperienceRaisingPet() && request.getExperience() != null) {
            throw ExperienceRequiredNullException.EXCEPTION;
        }

        Integer accountId = accountRepository.save(
                Account.builder()
                        .email(request.getEmail())
                        .password(encoder.encode(request.getPassword()))
                        .nickname(request.getNickname())
                        .age(request.getAge())
                        .sex(Sex.valueOf(request.getSex()))
                        .isExperienceRaisingPet(request.getIsExperienceRaisingPet())
                        .experienceDescription(request.getExperience())
                        .build()
        ).getId();

        return TokenResponse.builder()
                .accessToken(jwtTokenProvider.getAccessToken(accountId))
                .refreshToken(jwtTokenProvider.getRefreshToken(accountId))
                .build();
    }

    public void confirmEmail(String email) {
        accountRepository.findByEmail(email)
                .ifPresent(account -> {throw UserEmailAlreadyExistsException.EXCEPTION; });
    }

    public void confirmNickname(String nickname) {
        accountRepository.findByNickname(nickname)
                .ifPresent(account -> {throw UserNicknameAlreadyExistsException.EXCEPTION; });
    }

    public void saveCoordinate(CoordinatesRequest request) {
        Account me = getAccountNotBlocked();
        Double x = request.getLongitude();
        Double y = request.getLatitude();
        String division = getAdministrationDivision(x, y);
        String administrationDivision = division.substring(1, division.length() - 1);

        accountRepository.save(me.updateLocation(BigDecimal.valueOf(x), BigDecimal.valueOf(y), administrationDivision));
    }

    public HaveEverBeenEntrustedResponse haveEverBeenEntrusted(int id) {
        Account me = userUtil.getLocalConfirmAccount();
        Account target = accountRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        if (applicationRepository.findAllByVisitAccountAndMe(target, me).size() > 0) {
            return new HaveEverBeenEntrustedResponse(true);
        }
        return new HaveEverBeenEntrustedResponse(false);
    }

    public void makeReport(String comment, Integer id) {
        Account reporter = userUtil.getLocalConfirmAccount();
        Account target = accountRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        reportRepository.save(
                Report.builder()
                        .reason(comment)
                        .reporter(reporter)
                        .target(target)
                        .build()
        );
    }

    private String getAdministrationDivision(Double x, Double y) {
        ObjectMapper mapper = new ObjectMapper();
        String administrationDivision;
        HttpResponse<com.mashape.unirest.http.JsonNode> response;
        try {
             response = Unirest.get(KAKAO_API_URL + "x=" + x + "&y=" + y)
                    .header("Authorization", "KakaoAK " + restApiKey)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8")
                    .asJson();
        } catch(Exception e) {
            throw WrongLocationInformationException.EXCEPTION;
        }

        String json = response.getBody().toString();

        JsonNode document;
        try {
            document = mapper.readTree(json).path("documents").get(0);
        } catch (JsonProcessingException e) {
            throw FailedGetDesiredValueException.EXCEPTION;
        }

        if (!document.path("region_4depth_name").toString().equals("\"\"")) {
            administrationDivision = document.path("region_4depth_name").toString();
        } else {
            administrationDivision = document.path("region_3depth_name").toString();
        }

        return administrationDivision;
    }

    private Account getAccountNotBlocked() {
        return accountRepository.findByEmail(authenticationUtil.getUserEmail())
                .filter(Account::isNotBlocked)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

}
