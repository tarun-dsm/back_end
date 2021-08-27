package toyproject.syxxn.back_end;

import org.junit.jupiter.api.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.post.Post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApplicationControllerTest extends BaseTest {

    private MockMvc mvc;

    Integer postId;
    Account account1;
    Account account2;
    Account account3;
    Application application;

    @BeforeEach
    public void setUp() {
        mvc = setMvc();

        account1 = createAccount("test1@naver.com", true, "Tarun");
        account2 = createAccount("test2@naver.com", true, "Tarun");
        account3 = createAccount("test3@naver.com", true, "Tarun");
        createAccount("test4@naver.com", false, "Tarun");
        createAccount("test5@naver.com", true, "Tarun");

        Post post = createPost(account1, false, "2021-10-10");
        postId = post.getId();

        application = createApplication(account1, account2,false, false, "2021-10-10");
        createApplication(account1, account3,false, true, "2021-10-10");
        createApplication(account1, account2,false, false, "2021-08-10");
    }

    @WithMockUser(value = "test5@naver.com", password = "asdf123@")
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
        Post post = createPost(account1,true, "2021-05-10");
        mvc.perform(post("/application/"+post.getId()))
                .andExpect(status().isBadRequest()).andDo(print());
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

    @WithMockUser(value = "test3@naver.com", password = "asdf123@")
    @Test
    public void protectionApplication_409() throws Exception {
        mvc.perform(post("/application/"+ application.getPost().getId()))
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
        mvc.perform(delete("/application/"+application.getId()))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "test2@naver.com", password = "asdf123@")
    @Test
    public void cancelApplication_400() throws Exception {
        Integer id = createApplication(account1, account2, true, true, "2021-07-28").getId();
        mvc.perform(delete("/application/"+id))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "test3@naver.com", password = "asdf123@")
    @Test
    public void cancelApplication_404() throws Exception {
        mvc.perform(delete("/application/"+application.getId()))
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

        mvc.perform(patch("/application/"+application.getId()))
                .andExpect(status().isNoContent()).andDo(print());
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void acceptApplication_400() throws Exception {
        Integer id = createApplication(account1, account2,true, false,"2021-10-10").getId();
        mvc.perform(patch("/application/"+id))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "test3@naver.com", password = "asdf123@")
    @Test
    public void acceptApplication_401() throws Exception {
        mvc.perform(patch("/application/"+application.getId()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void getApplications() throws Exception {
        mvc.perform(get("/application/post/"+application.getPost().getId()))
                .andExpect(status().isOk()).andDo(print());
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void getApplications_400() throws Exception {
        Post post = createPost(account1, true, "2021-05-10");

        mvc.perform(get("/application/post/"+post.getId()))
                .andExpect(status().isNotFound());
    }

}
