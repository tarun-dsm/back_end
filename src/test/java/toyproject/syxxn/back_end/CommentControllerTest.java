package toyproject.syxxn.back_end;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.syxxn.back_end.dto.request.CommentRequest;
import toyproject.syxxn.back_end.entity.account.Account;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends BaseTest {

    private MockMvc mvc;

    Account account;
    Integer postId;

    @BeforeEach
    public void setUp() {
        mvc = setMvc();

        account = createAccount("adsf1234@naver.com", "Tarun", true);
        accountRepository.save(account.updateLocation(BigDecimal.valueOf(37.5668260000), BigDecimal.valueOf(126.9786567000), "동동동"));
        Account account2 = createAccount("adsf123@naver.com", "asdf", true);
        accountRepository.save(account2.updateLocation(BigDecimal.valueOf(37.5668260000), BigDecimal.valueOf(126.978), "동동동"));

        postId = createPost(account2, false, "2021-09-29").getId();
    }

    @WithMockUser(value = "adsf123@naver.com", password = "asdf123@")
    @Test
    public void write_comment() throws Exception {
        mvc.perform(post("/comment/" + postId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new CommentRequest("어머어머 안녕하세요^^*")))
        ).andExpect(status().isCreated());

        mvc.perform(get("/comments/post/" + postId)
        ).andExpect(status().isOk()).andDo(print());
    }

}
