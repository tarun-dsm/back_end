package toyproject.syxxn.back_end;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import toyproject.syxxn.back_end.dto.request.SignInRequest;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshToken;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshTokenRepository;
import toyproject.syxxn.back_end.security.jwt.JwtTokenProvider;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackEndApplication.class)
@ActiveProfiles("test")
public class AuthControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder encoder;

    String refreshToken;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        Account account = accountRepository.save(
                Account.builder()
                        .email("test1@naver.com")
                        .password(encoder.encode("asdf123@"))
                        .sex(Sex.MALE)
                        .nickname("나는야1번")
                        .age(18)
                        .isExperienceRasingPet(false)
                        .experience(null)
                        .address("경기도 서울시 구성동")
                        .isLocationConfirm(true)
                        .build()
        );

        refreshToken = refreshTokenRepository.save(
                RefreshToken.builder()
                        .accountId(account.getId())
                        .refreshExp(1234L)
                        .refreshToken(jwtTokenProvider.generateRefreshToken(account.getId()))
                        .build()
        ).getRefreshToken();

    }

    @AfterEach
    public void deleteAll() {
        accountRepository.deleteAll();
        refreshTokenRepository.deleteAll();
    }

    @Test
    public void login() throws Exception {
        SignInRequest request = new SignInRequest("test1@naver.com", "asdf123@");

        mvc.perform(post("/auth")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    public void login_400() throws Exception {
        SignInRequest request = new SignInRequest("test1@naver.com", "asdf15@");

        mvc.perform(post("/auth")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void login_404() throws Exception {
        SignInRequest request = new SignInRequest("test1@naver.com", "asdf1255@");

        mvc.perform(post("/auth")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void tokenRefresh() throws Exception {
        mvc.perform(put("/auth")
                .header("X-Refresh-Token",refreshToken)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void tokenRefresh_401() throws Exception {
        mvc.perform(put("/auth")
                .header("X-Refresh-Token","asdf.asdf.asdf")
        ).andExpect(status().isUnauthorized());
    }

}
