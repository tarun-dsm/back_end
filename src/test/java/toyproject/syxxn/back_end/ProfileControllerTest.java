package toyproject.syxxn.back_end;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.syxxn.back_end.entity.account.Account;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileControllerTest extends BaseTest {

    private MockMvc mvc;

    Account account1;
    Account account2;
    Account account3;

    @BeforeEach
    public void setUp() {
        mvc = setMvc();

        account1 = createAccount("test1@naver.com", true, "Tarun");
        account2 = createAccount("test2@naver.com", true, "Tarun");
        account3 = createAccount("test3@naver.com", false, "Tarun");
        Account account4 = createAccount("test4@naver.com", true);
        Account account5 = createAccount("test5@naver.com", true);


        createApplication(account1, account2, true);

        createPost(account1);
        createPost(account1);
        createPost(account2);
        createPost(account2);

        createReview(account1, account2, BigDecimal.valueOf(3.2));
        createReview(account1, account2, BigDecimal.valueOf(3.7));
        createReview(account1, account2, BigDecimal.valueOf(4.2));
        createReview(account2, account1, BigDecimal.valueOf(5.0));
        createReview(account2, account1, BigDecimal.valueOf(4.8));

        createReview(account4, account5, BigDecimal.valueOf(2.8));
        createReview(account4, account5, BigDecimal.valueOf(2.8));
        createReview(account5, account4, BigDecimal.valueOf(1.8));
        createReview(account5, account4, BigDecimal.valueOf(1.8));
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void getProfile() throws Exception {
        mvc.perform(get("/profile/"+account1.getId())
        ).andExpect(status().isOk());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void getProfile_mine() throws Exception {
        mvc.perform(get("/profile")
        ).andExpect(status().isOk()).andDo(print());
    }

    @WithMockUser(value = "test3@naver.com", password = "asdf123@")
    @Test
    public void getProfile_account3() throws Exception {
        mvc.perform(get("/profile")
        ).andExpect(status().isOk()).andDo(print());
    }

    @WithMockUser(value = "test4@naver.com", password = "asdf123@")
    @Test
    public void getProfile_account4() throws Exception {
        mvc.perform(get("/profile")
        ).andExpect(status().isOk()).andDo(print());
    }

    @WithMockUser(value = "test5@naver.com", password = "asdf123@")
    @Test
    public void getProfile_account5() throws Exception {
        mvc.perform(get("/profile")
        ).andExpect(status().isOk()).andDo(print());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void getProfile_other() throws Exception {
        mvc.perform(get("/profile/"+account2.getId())
        ).andExpect(status().isOk());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void getProfile_404() throws Exception {
        mvc.perform(get("/profile/1234")
        ).andExpect(status().isNotFound());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void getReviews() throws Exception {
        mvc.perform(get("/profile/reviews/"+account1.getId())
        ).andExpect(status().isOk());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void getReviews_mine() throws Exception {
        mvc.perform(get("/profile/reviews")
        ).andExpect(status().isOk());
    }

    @WithMockUser(value = "test3@naver.com", password = "asdf123@")
    @Test
    public void getReviews_account3() throws Exception {
        mvc.perform(get("/profile/reviews")
        ).andExpect(status().isOk());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void getPosts() throws Exception {
        mvc.perform(get("/profile/posts/"+account1.getId())
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void getPosts_mine() throws Exception {
        mvc.perform(get("/profile/posts")
        ).andExpect(status().isOk());
    }

    @WithMockUser(value = "test3@naver.com", password = "asdf123@")
    @Test
    public void getPosts_account3() throws Exception {
        mvc.perform(get("/profile/posts")
        ).andExpect(status().isOk());
    }

}
