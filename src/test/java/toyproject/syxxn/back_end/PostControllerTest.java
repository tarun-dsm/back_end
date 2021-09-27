package toyproject.syxxn.back_end;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.account.Account;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class PostControllerTest extends BaseTest {

    private MockMvc mvc;

    Account account;
    Integer postId;

    @BeforeEach
    public void setUp() {
        mvc = setMvc();

        account = createAccount("adsf1234@naver.com", true, "Tarun");
        createAccount("test1@naver.com", true);
        createAccount("test1234@gmail.com", false, "ggosunnae");

        postId = createPetInfo(account).getPost().getId();
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
        mvc.perform(delete("/post/" + postId)).andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "test1234@gmail.com", password = "asdf123@")
    @Test
    public void deletePost_401() throws Exception {
        mvc.perform(delete("/post/" + postId))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void deletePost_401_() throws Exception {
        mvc.perform(delete("/post/" + postId))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "adsf1234@naver.com", password = "asdf123@")
    @Test
    public void updatePost() throws Exception {
        PostRequest request = createPostRequest();
        mvc.perform(patch("/post/" + postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
        ).andDo(print()).andExpect(status().isOk());
    }

    @WithMockUser(value = "adsf1234@naver.com", password = "asdf123@")
    @Test
    public void updatePost_404() throws Exception {
        PostRequest request = createPostRequest();
        mvc.perform(patch("/post/" + 123456)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
        ).andExpect(status().isNotFound());
    }

    @WithMockUser(value = "adsf1234@naver.com", password = "asdf123@")
    @Test
    public void writePost() throws Exception {
        PostRequest request = createPostRequest();

        mvc.perform(post("/post")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isCreated());
    }

    @WithMockUser(value = "adsf1234@naver.com", password = "asdf123@")
    @Test
    public void writePost_400() throws Exception {
        PostRequest request = PostRequest.builder()
                .title("이것은 제목")
                .description("이것은 설명")
                .protectionStartDate("2021-08-06")
                .protectionEndDate("2021-08-05")
                .applicationEndDate("2021-08-04")
                .contactInfo("010-0000-0000")
                .petName("또로")
                .petSpecies("코리안숏헤어")
                .petSex(Sex.MALE.toString())
                .build();

        mvc.perform(post("/post")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "adsf1234@naver.com", password = "asdf123@")
    @Test
    public void getPostDetails() throws Exception {
        mvc.perform(get("/post/"+postId)
        ).andExpect(status().isOk());
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void getPostDetails_() throws Exception {
        mvc.perform(get("/post/"+postId)
        ).andExpect(status().isOk());
    }

    @WithMockUser(value = "test1234@gmail.com", password = "asdf123@")
    @Test
    public void getPosts_locationConfirmFalse() throws Exception {
        mvc.perform(get("/post")
        ).andExpect(status().isOk());
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void getPosts_locationConfirmTrue() throws Exception {
        mvc.perform(get("/post")
        ).andExpect(status().isOk());
    }

}
