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
import toyproject.syxxn.back_end.entity.report.Report;
import toyproject.syxxn.back_end.entity.report.ReportRepository;
import toyproject.syxxn.back_end.entity.verify.VerifyNumber;
import toyproject.syxxn.back_end.entity.verify.VerifyNumberRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.service.util.AuthenticationUtil;
import toyproject.syxxn.back_end.service.util.TokenUtil;
import toyproject.syxxn.back_end.service.util.UserUtil;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ReportRepository reportRepository;
    private final VerifyNumberRepository verifyNumberRepository;

    private final AuthenticationUtil authenticationFacade;

    private final PasswordEncoder encoder;

    @Value("${kakao.api-key}")
    private String restApiKey;

    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=";

    private final UserUtil baseService;
    private final TokenUtil tokenUtil;

    @Transactional
    public TokenResponse signUp(SignUpRequest request) {
        verifyNumberRepository.findById(request.getEmail())
                .filter(VerifyNumber::isVerified)
                .orElseThrow(UserNotUnauthenticatedException::new);

        Integer accountId = accountRepository.save(
                Account.builder()
                        .email(request.getEmail())
                        .password(encoder.encode(request.getPassword()))
                        .nickname(request.getNickname())
                        .age(request.getAge())
                        .sex(Sex.valueOf(request.getSex()))
                        .isExperienceRaisingPet(request.isExperienceRasingPet())
                        .experience(request.getExperience())
                        .build()
        ).getId();

        String refreshToken = tokenUtil.getRefreshToken(accountId);

        return TokenResponse.builder()
                .accessToken(tokenUtil.getAccessToken(accountId))
                .refreshToken(refreshToken)
                .build();
    }

    public void confirmEmail(String email) {
        accountRepository.findByEmail(email)
                .ifPresent(account -> {throw new UserEmailAlreadyExistsException(); });
    }

    public void confirmNickname(String nickname) {
        accountRepository.findByNickname(nickname)
                .ifPresent(account -> {throw new UserNicknameAlreadyExistsException(); });
    }

    @Transactional
    public void saveCoordinate(CoordinatesRequest request) throws JsonProcessingException, UnirestException {
        Account account = accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .filter(baseService::isNotBlocked)
                .orElseThrow(UserNotFoundException::new);
        Double x = request.getLongitude();
        Double y = request.getLatitude();
        String division = getAdministrationDivision(x, y);
        String administrationDivision = division.substring(1, division.length() - 1);

        account.updateLocation(BigDecimal.valueOf(x), BigDecimal.valueOf(y), administrationDivision);
    }

    public void makeReport(String comment, Integer id) {
        Account reporter = baseService.getLocalConfirmAccount();
        Account target = accountRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        reportRepository.save(
                Report.builder()
                        .comment(comment)
                        .reporter(reporter)
                        .target(target)
                        .build()
        );
    }

    private String getAdministrationDivision(Double x, Double y) throws JsonProcessingException, UnirestException {
        ObjectMapper mapper = new ObjectMapper();
        String administrationDivision = "";
        HttpResponse<com.mashape.unirest.http.JsonNode> response;
        try {
             response = Unirest.get(KAKAO_API_URL + x + "&y=" + y)
                    .header("Authorization", "KakaoAK " + restApiKey)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8")
                    .asJson();
        } catch(Exception e) {
            throw new WrongLocationInformationException();
        }

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
