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
import toyproject.syxxn.back_end.dto.request.SignUpRequest;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.verify.VerifyNumber;
import toyproject.syxxn.back_end.entity.verify.VerifyNumberRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackEndApplication.class)
@ActiveProfiles("test")
public class AccountControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VerifyNumberRepository verifyNumberRepository;

    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        accountRepository.save(
                Account.builder()
                        .email("adsf1234@naver.com")
                        .password(encoder.encode("asdf1234"))
                        .sex(Sex.FEMALE)
                        .nickname("Tarun")
                        .age(18)
                        .isExperienceRasingPet(false)
                        .experience(null)
                        .address("경기도 서울시 구성동")
                        .isLocationConfirm(false)
                .build()
        );

        verifyNumberRepository.save(
                new VerifyNumber("2000ls@gmail.com", "123456", true)
        );

    }

    @AfterEach
    public void deleteAll() {
        accountRepository.deleteAll();
    }

    @Test
    public void confirm_email() throws Exception {
        mvc.perform(get("/account/email/2011413lsy@dsm.hs.kr")
        ).andExpect(status().isOk());
    }

    @Test
    public void confirm_email_400() throws Exception {
        mvc.perform(get("/account/email/2011413lsy")
        ).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void confirm_email_409() throws Exception {
        mvc.perform(get("/account/email/adsf1234@naver.com")
        ).andExpect(status().isConflict()).andDo(print());
    }

    @Test
    public void confirm_nickname_english() throws Exception {
        mvc.perform(get("/account/nickname/asdfqwertf")
                .characterEncoding("UTF-8")
        ).andExpect(status().isOk());
    }

    @Test
    public void confirm_nickname_number() throws Exception {
        mvc.perform(get("/account/nickname/1234567890")
                .characterEncoding("UTF-8")
        ).andExpect(status().isOk());
    }

    @Test
    public void confirm_nickname_korean() throws Exception {
        mvc.perform(get("/account/nickname/내맘대로할거야글자수")
                .characterEncoding("UTF-8")
        ).andExpect(status().isOk());
    }

    @Test
    public void confirm_nickname_fail1() throws Exception {
        mvc.perform(get("/account/nickname/띄어쓰기도 가능함")
                .characterEncoding("UTF-8")
        ).andExpect(status().isOk());
    }

    @Test
    public void confirm_nickname_400() throws Exception {
        mvc.perform(get("/account/nickname/띄")
                .characterEncoding("UTF-8")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void confirm_nickname_409() throws Exception {
        mvc.perform(get("/account/nickname/Tarun")
                .characterEncoding("UTF-8")
        ).andExpect(status().isConflict());
    }

    @Test
    public void sign_up() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .email("2000ls@gmail.com")
                .password(encoder.encode("passpa123@"))
                .nickname("닉네임야호")
                .age(120)
                .sex("FEMALE")
                .isExperienceRasingPet(true)
                .experience("야호")
                .address("경상북도 수원시 죽전동")
                .build();

        mvc.perform(post("/account")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isCreated());
    }

}
