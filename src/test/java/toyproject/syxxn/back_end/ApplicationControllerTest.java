package toyproject.syxxn.back_end;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackEndApplication.class)
@ActiveProfiles("test")
public class ApplicationControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private PasswordEncoder encoder;

    Long post_id;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        Account account = accountRepository.save(
                Account.builder()
                        .email("adsf1234@naver.com")
                        .password(encoder.encode("asdf123@4"))
                        .sex(Sex.FEMALE)
                        .nickname("Tarun")
                        .age(18)
                        .isExperienceRasingPet(false)
                        .experience(null)
                        .address("경기도 서울시 구성동")
                        .isLocationConfirm(false)
                        .build()
        );

        Post post = postRepository.save(
                Post.builder()
                        .account(account)
                        .applicationEndDate(LocalDate.of(2021,10,22))
                        .isWithinADay(false)
                        .tripStartDate(LocalDate.of(2021,10,24))
                        .tripEndDate(LocalDate.of(2021,10,29))
                        .contactInfo("010-0000-0000")
                        .description("랄랄라")
                        .isUpdated(false)
                        .build()
        );

        post_id = post.getId();

        applicationRepository.save(
                Application.builder()
                        .post(post)
                        .account(account)
                        .build()
        );
    }

    @AfterEach
    public void deleteAll() {
/*        accountRepository.deleteAll();
        applicationRepository.deleteAll();
        postRepository.deleteAll();*/
    }

    @WithMockUser(value = "1223", password = "123")
    @Test
    public void protectionApplication_401() throws Exception {
        mvc.perform(post("/application/1")).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "1", password = "asdf1234@")
    @Test
    public void protectionApplication() throws Exception {
        mvc.perform(post("/application/1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "10", password = "asdf1234@")
    @Test
    public void protectionApplication_404() throws Exception {
        mvc.perform(post("/application/1234")).andDo(print())
                .andExpect(status().isNotFound());
    }

}
