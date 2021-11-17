package toyproject.syxxn.back_end;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.syxxn.back_end.dto.request.EmailRequest;
import toyproject.syxxn.back_end.dto.request.VerifyRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmailControllerTest extends BaseTest {

    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        mvc = setMvc();

        createAccount("test1@naver.com", "Tarun", false);
        createVerifyNumber();
    }

    /*@Test
    public void sendVerifyNumberEmail() throws Exception {
        mvc.perform(post("/email")
                .content(new ObjectMapper().writeValueAsString(new EmailRequest("test3@gmail.com")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void sendVerifyNumberEmail_() throws Exception {
        mvc.perform(post("/email")
                .content(new ObjectMapper().writeValueAsString(new EmailRequest("test2@dsm.hs.kr")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }*/

    @Test
    public void sendVerifyNumberEmail_400_1() throws Exception {
        mvc.perform(post("/email")
                .content(new ObjectMapper().writeValueAsString(new EmailRequest("test2@dsm.hs.kr")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void sendVerifyNumberEmail_400_2() throws Exception {
        mvc.perform(post("/email")
                .content(new ObjectMapper().writeValueAsString(new EmailRequest("test3")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void sendVerifyNumberEmail_409() throws Exception {
        mvc.perform(post("/email")
                .content(new ObjectMapper().writeValueAsString(new EmailRequest("test1@naver.com")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isConflict());
    }

    @Test
    public void verify() throws Exception {
        mvc.perform(get("/email")
                .content(new ObjectMapper().writeValueAsString(new VerifyRequest("2000ls@gmail.com","123456")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void verify_400() throws Exception {
        mvc.perform(get("/email")
                .content(new ObjectMapper().writeValueAsString(new VerifyRequest("test2","123456")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void verify_400_2() throws Exception {
        mvc.perform(get("/email")
                .content(new ObjectMapper().writeValueAsString(new VerifyRequest("test2@dsm.hs.kr","123856")))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

}
