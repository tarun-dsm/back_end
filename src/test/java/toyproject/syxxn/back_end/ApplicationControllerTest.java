package toyproject.syxxn.back_end;

import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    Integer postId;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        Account account = accountRepository.save(
                Account.builder()
                        .email("test0000@naver.com")
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

        Account account2 = accountRepository.save(
                Account.builder()
                        .email("test1234@naver.com")
                        .password(encoder.encode("asdf123@"))
                        .sex(Sex.FEMALE)
                        .nickname("나는야2번")
                        .age(18)
                        .isExperienceRasingPet(false)
                        .experience(null)
                        .address("경기도 서울시 구성동")
                        .isLocationConfirm(true)
                        .build()
        );

        accountRepository.save(
                Account.builder()
                        .email("test5678@naver.com")
                        .password(encoder.encode("asdf123@"))
                        .sex(Sex.FEMALE)
                        .nickname("나는야3번")
                        .age(18)
                        .isExperienceRasingPet(false)
                        .experience(null)
                        .address("경기도 서울시 구성동")
                        .isLocationConfirm(true)
                        .build()
        );

        accountRepository.save(
                Account.builder()
                        .email("test9101@naver.com")
                        .password(encoder.encode("asdf123@"))
                        .sex(Sex.MALE)
                        .nickname("나는야4번")
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
                        .protectionStartDate(LocalDate.of(2021,10,24))
                        .protectionEndDate(LocalDate.of(2021,10,29))
                        .contactInfo("010-0000-0000")
                        .description("랄랄라")
                        .isUpdated(false)
                        .build()
        );
        postId = post.getId();

        applicationRepository.save(
                Application.builder()
                        .post(post)
                        .account(account2)
                        .isAccepted(false)
                        .build()
        );
    }

    @AfterEach
    public void deleteAll() {
        accountRepository.deleteAll();
        applicationRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Order(1)
    @WithMockUser(value = "3", password = "asdf123@")
    @Test
    public void protectionApplication() throws Exception {
        mvc.perform(post("/application/"+postId)).andDo(print())
                .andExpect(status().isOk());
    }

    @Order(2)
    @WithMockUser(value = "5", password = "asdf123@")
    @Test
    public void protectionApplication_400() throws Exception {
        mvc.perform(post("/application/"+postId))
                .andExpect(status().isBadRequest());
    }

    @Order(3)
    @WithMockUser(value = "1223", password = "123")
    @Test
    public void protectionApplication_401() throws Exception {
        mvc.perform(post("/application/"+postId))
                .andExpect(status().isUnauthorized());
    }

    @Order(4)
    @WithMockUser(value = "13", password = "asdf123@")
    @Test
    public void protectionApplication_404() throws Exception {
        mvc.perform(post("/application/112"))
                .andExpect(status().isNotFound());
    }

    @Order(5)
    @WithMockUser(value = "18", password = "asdf123@")
    @Test
    public void protectionApplication_409() throws Exception {
        mvc.perform(post("/application/"+postId))
                .andExpect(status().isConflict());
    }

    @Order(6)
    @WithMockUser(value = "24", password = "123")
    @Test
    public void protectionApplication_401_2() throws Exception {
        mvc.perform(post("/application/"+postId))
                .andExpect(status().isUnauthorized());
    }

}
