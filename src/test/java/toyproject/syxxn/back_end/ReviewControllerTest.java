package toyproject.syxxn.back_end;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import toyproject.syxxn.back_end.dto.request.ReviewRequest;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.entity.review.Review;
import toyproject.syxxn.back_end.entity.review.ReviewRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ReviewControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PasswordEncoder encoder;

    Account account;
    Account account2;
    Application application;
    Application notDoneApplication;

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

        notDoneApplication = createApplication(account2,true, true, "2021-10-22");
        application = createApplication(account2,true, true, "2021-05-22");
        createApplication(account2,true, false, "2021-05-22");
    }

    @AfterEach
    public void deleteAll() {
        accountRepository.deleteAll();
        applicationRepository.deleteAll();
        reviewRepository.deleteAll();
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void writeReview() throws Exception {
        ReviewRequest request = createRequest();

        mvc.perform(post("/review/"+application.getId())
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isCreated());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void writeReview_() throws Exception {
        ReviewRequest request = createRequest();

        mvc.perform(post("/review/"+application.getId())
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isCreated());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void writeReview_400() throws Exception {
        ReviewRequest request = createRequest();

        mvc.perform(post("/review/"+notDoneApplication.getId())
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void writeReview_401() throws Exception {
        ReviewRequest request = createRequest();

        mvc.perform(post("/review/"+application.getId())
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "test4@naver.com", password = "asdf123@")
    @Test
    public void writeReview_401_2() throws Exception {
        ReviewRequest request = createRequest();

        mvc.perform(post("/review/"+application.getId())
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "test3@naver.com", password = "asdf123@")
    @Test
    public void writeReview_401_3() throws Exception {
        ReviewRequest request = createRequest();

        mvc.perform(post("/review/"+application.getId())
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void writeReview_404() throws Exception {
        ReviewRequest request = createRequest();

        mvc.perform(post("/review/12345")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNotFound());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void writeReview_409() throws Exception {
        Integer id = createReview().getApplication().getId();
        ReviewRequest request = createRequest();

        mvc.perform(post("/review/"+id)
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isConflict());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void deleteReview() throws Exception {
        Integer id = createReview().getId();

        mvc.perform(delete("/review/"+id)
        ).andExpect(status().isOk());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void deleteReview_404() throws Exception {
        createReview();

        mvc.perform(delete("/review/456")
        ).andExpect(status().isNotFound());
    }

    @WithMockUser(value = "test3@naver.com", password = "asdf123@")
    @Test
    public void deleteReview_404_() throws Exception {
        Integer id = createReview().getId();

        mvc.perform(delete("/review/"+id)
        ).andExpect(status().isNotFound());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void updateReview() throws Exception {
        Integer id = createReview().getId();
        ReviewRequest request = createRequest();

        mvc.perform(put("/review/"+id)
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNoContent());
    }

    @WithMockUser(value = "test3@naver.com", password = "asdf123@")
    @Test
    public void updateReview_401() throws Exception {
        Integer id = createReview().getId();
        ReviewRequest request = createRequest();

        mvc.perform(put("/review/"+id)
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNotFound());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void updateReview_404() throws Exception {
        createReview();
        ReviewRequest request = createRequest();

        mvc.perform(put("/review/4561")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void reviewTest() {
        Review review = createReview();
        assertNotNull(review.getId());
        assertNotNull(review.getGrade());
        assertNotNull(review.getComment());
        assertNotNull(review.getWriter());
        assertNotNull(review.getTarget());
        assertNotNull(review.getApplication());
    }

    private ReviewRequest createRequest() {
        return ReviewRequest.builder()
                .grade(BigDecimal.valueOf(2.3))
                .comment("울랄라 리뷰")
                .build();
    }

    private Review createReview() {
        return reviewRepository.save(
                Review.builder()
                        .target(account)
                        .writer(account2)
                        .application(createApplication(account2, true, true, "2021-05-22"))
                        .grade(BigDecimal.valueOf(4.5))
                        .comment("대체 왜 ratingScore 안에는 값이 안들어가는건지 모르겠음 어이없어")
                        .build()
        );
    }

    private Application createApplication(Account account, boolean isEnd, boolean isAccepted, String endDate) {
        return applicationRepository.save(
                Application.builder()
                        .post(createPost(isEnd, endDate))
                        .account(account)
                        .isAccepted(isAccepted)
                        .build()
        );
    }

    private Post createPost(boolean isEnd, String endDate) {
        return postRepository.save(
                Post.builder()
                        .account(account)
                        .title("제목을 까먹었지 뭐야..")
                        .applicationEndDate(LocalDate.parse(endDate))
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