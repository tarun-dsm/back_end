package toyproject.syxxn.back_end;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class PostControllerTest extends BaseTest {

    private MockMvc mvc;

    Account account;
    Post post;

    @BeforeEach
    public void setUp() {
        mvc = setMvc();

        account = createAccount("adsf1234@naver.com", true, "Tarun");
        createAccount("test1234@gmail.com", false, "ggosunnae");

        post = createPost(account, false, "2021-09-01");
    }

    @WithMockUser(value = "adsf1234@naver.com", password = "asdf123@")
    @Test
    public void deletePost_404() throws Exception {
        mvc.perform(delete("/post/123"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(value = "adsf1234@naver.com", password = "asdf123@")
    @Test
    public void deletePost() throws Exception {
        mvc.perform(delete("/post/" + post.getId())).andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "test1234@gmail.com", password = "asdf123@")
    @Test
    public void deletePost_401() throws Exception {
        mvc.perform(delete("/post/" + post.getId()))
                .andExpect(status().isUnauthorized());
    }


}
