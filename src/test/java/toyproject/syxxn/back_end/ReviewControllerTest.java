package toyproject.syxxn.back_end;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.syxxn.back_end.dto.request.ReviewRequest;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.review.Review;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewControllerTest extends BaseTest{

    private MockMvc mvc;

    Account account1;
    Account account2;
    Application application;
    Application notDoneApplication;
    Review review;

    @BeforeEach
    public void setUp() {
        mvc = setMvc();

        account1 = createAccount("test1@naver.com", "Tarun1", true);
        account2 = createAccount("test2@naver.com", "Tarun2", true);
        createAccount("test3@naver.com", "Tarun3", true);
        createAccount("test4@naver.com", "Tarun4", false);

        notDoneApplication = createApplication(account1, account2,true, true, "2021-10-22");
        application = createApplication(account1, account2,true, true, "2021-05-22");
        createApplication(account1, account2,true, false, "2021-05-22");

        review = createReview(account1, account2, BigDecimal.valueOf(1.2));
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
        ).andDo(print()).andExpect(status().isCreated());
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
        Integer id = createReview(account1, account2, BigDecimal.valueOf(5.0)).getApplication().getId();
        ReviewRequest request = createRequest();

        mvc.perform(post("/review/"+id)
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isConflict());
    }

    @WithMockUser(value = "test2@naver.com")
    @Test
    public void deleteReview() throws Exception {
        Integer id = createReview(account1, account2, BigDecimal.valueOf(3.2)).getId();

        mvc.perform(delete("/review/"+id)
        ).andExpect(status().isOk()).andDo(print());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void deleteReview_404() throws Exception {
        mvc.perform(delete("/review/456")
        ).andExpect(status().isNotFound());
    }

    @WithMockUser(value = "test3@naver.com", password = "asdf123@")
    @Test
    public void deleteReview_404_() throws Exception {
        mvc.perform(delete("/review/"+review.getId())
        ).andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void updateReview() throws Exception {
        ReviewRequest request = createRequest();

        mvc.perform(patch("/review/"+review.getId())
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNoContent());
    }

    @WithMockUser(value = "test3@naver.com", password = "asdf123@")
    @Test
    public void updateReview_401() throws Exception {
        ReviewRequest request = createRequest();

        mvc.perform(patch("/review/"+review.getId())
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void updateReview_404() throws Exception {
        ReviewRequest request = createRequest();

        mvc.perform(patch("/review/4561")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void reviewTest() {
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

}