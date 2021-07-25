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
import toyproject.syxxn.back_end.dto.request.EmailRequest;
import toyproject.syxxn.back_end.dto.request.VerifyRequest;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.verify.VerifyNumber;
import toyproject.syxxn.back_end.entity.verify.VerifyNumberRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackEndApplication.class)
@ActiveProfiles("test")
public class EmailControllerTest {

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

        verifyNumberRepository.save(
                new VerifyNumber("test2@dsm.hs.kr","123456",false)
        );
    }

    @AfterEach
    public void deleteAll() {
        accountRepository.deleteAll();
        verifyNumberRepository.deleteAll();
    }

    /*@Test
    public void sendVerifyNumberEmail() throws Exception {
        mvc.perform(post("/email")
                .content(new ObjectMapper().writeValueAsString(new EmailRequest("test3@gmail.com")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void sendVerifyNumberEmail_() throws Exception {
        mvc.perform(post("/email")
                .content(new ObjectMapper().writeValueAsString(new EmailRequest("test2@dsm.hs.kr")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }*/

    @Test
    public void sendVerifyNumberEmail_400() throws Exception {
        mvc.perform(post("/email")
                .content(new ObjectMapper().writeValueAsString(new EmailRequest("test3")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void sendVerifyNumberEmail_409() throws Exception {
        mvc.perform(post("/email")
                .content(new ObjectMapper().writeValueAsString(new EmailRequest("test1@naver.com")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isConflict());
    }

    @Test
    public void verify() throws Exception {
        mvc.perform(patch("/email")
                .content(new ObjectMapper().writeValueAsString(new VerifyRequest("test2@dsm.hs.kr","123456")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void verify_400() throws Exception {
        mvc.perform(patch("/email")
                .content(new ObjectMapper().writeValueAsString(new VerifyRequest("test2","123456")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void verify_400_2() throws Exception {
        mvc.perform(patch("/email")
                .content(new ObjectMapper().writeValueAsString(new VerifyRequest("test2@dsm.hs.kr","123856")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

}
