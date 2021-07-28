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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    Integer postId;
    Account account;
    Account account2;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        account = accountRepository.save(
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

        account2 = accountRepository.save(
                Account.builder()
                        .email("test2@naver.com")
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
                        .email("test3@naver.com")
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
                        .email("test4@naver.com")
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

        Post post = createPost(false);
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

    @WithMockUser(value = "test3@naver.com", password = "asdf123@")
    @Test
    public void protectionApplication() throws Exception {
        mvc.perform(post("/application/"+postId))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void protectionApplication_400() throws Exception {
        mvc.perform(post("/application/"+postId))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void protectionApplication_400_2() throws Exception {
        Post post = createPost(true);
        mvc.perform(post("/application/"+post.getIsApplicationEnd()))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "test@naver.com", password = "123")
    @Test
    public void protectionApplication_401() throws Exception {
        mvc.perform(post("/application/"+postId))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void protectionApplication_404() throws Exception {
        mvc.perform(post("/application/112"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void protectionApplication_409() throws Exception {
        mvc.perform(post("/application/"+postId))
                .andExpect(status().isConflict());
    }

    @WithMockUser(value = "test4@naver.com", password = "123")
    @Test
    public void protectionApplication_401_2() throws Exception {
        mvc.perform(post("/application/"+postId))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void cancelApplication() throws Exception {
        Integer applicationId = createApplication(false, false).getId();

        mvc.perform(delete("/application/"+applicationId))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void cancelApplication_400() throws Exception {
        Integer applicationId = createApplication(true, false).getId();

        mvc.perform(delete("/application/"+applicationId))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "test3@naver.com", password = "asdf123@")
    @Test
    public void cancelApplication_404() throws Exception {
        Integer applicationId = createApplication(false, false).getId();

        mvc.perform(delete("/application/"+applicationId))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void getMyApplications() throws Exception {
        mvc.perform(get("/application"))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void acceptApplication() throws Exception {
        Integer applicationId = createApplication(false, false).getId();
        mvc.perform(patch("/application/"+applicationId))
                .andExpect(status().isNoContent()).andDo(print());
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void acceptApplication_400() throws Exception {
        Integer applicationId = createApplication(true, false).getId();
        mvc.perform(patch("/application/"+applicationId))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "test3@naver.com", password = "asdf123@")
    @Test
    public void acceptApplication_401() throws Exception {
        Integer applicationId = createApplication(false, false).getId();
        mvc.perform(patch("/application/"+applicationId))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void getApplications() throws Exception {
        mvc.perform(get("/application/post/"+postId))
                .andExpect(status().isOk()).andDo(print());
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void getApplications_400() throws Exception {
        Post post = createPost(true);

        mvc.perform(get("/application/post/"+post.getId()))
                .andExpect(status().isNotFound());
    }

    private Application createApplication(boolean isEnd, boolean isAccepted) {
        return applicationRepository.save(
                Application.builder()
                        .post(createPost(isEnd))
                        .account(account2)
                        .isAccepted(isAccepted)
                        .build()
        );
    }

    private Post createPost(boolean isEnd) {
        return postRepository.save(
                Post.builder()
                        .account(account)
                        .title("제목을 까먹었지 뭐야..")
                        .applicationEndDate(LocalDate.of(2021,10,22))
                        .protectionStartDate(LocalDate.of(2021,10,24))
                        .protectionEndDate(LocalDate.of(2021,10,29))
                        .contactInfo("010-0000-0000")
                        .description("랄랄라")
                        .isUpdated(false)
                        .isApplicationEnd(isEnd)
                        .build()
        );
    }

}
