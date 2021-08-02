package toyproject.syxxn.back_end;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.syxxn.back_end.entity.account.Account;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackEndApplication.class)
@ActiveProfiles("test")
public class ProfileControllerTest extends BaseTest {

    private MockMvc mvc;

    Account account1;
    Account account2;

    @BeforeEach
    public void setUp() {
        mvc = setMvc();

        account1 = createAccount("test1@naver.com", true, "Tarun");
        account2 = createAccount("test2@naver.com", false, "Tarun");

        createApplication(account1, account2,true, true, "2021-05-22");

        createReview(account1, account2, BigDecimal.valueOf(3.2));
        createReview(account1, account2, BigDecimal.valueOf(3.7));
        createReview(account1, account2, BigDecimal.valueOf(4.2));
        createReview(account2, account1, BigDecimal.valueOf(2.2));
        createReview(account2, account1, BigDecimal.valueOf(0.2));
    }

    @AfterEach
    public void deleteAll() {
        deleteEvery();
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
        ).andExpect(status().isOk());
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

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void getPosts() throws Exception {
        mvc.perform(get("/profile/posts/"+account1.getId())
        ).andExpect(status().isOk());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void getPosts_mine() throws Exception {
        mvc.perform(get("/profile/posts")
        ).andExpect(status().isOk());
    }

}
