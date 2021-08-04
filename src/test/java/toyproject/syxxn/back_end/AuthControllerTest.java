package toyproject.syxxn.back_end;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.syxxn.back_end.dto.request.SignInRequest;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshToken;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerTest extends BaseTest {

    private MockMvc mvc;

    RefreshToken refreshToken;

    @BeforeEach
    public void setUp() {
        mvc = setMvc();

        Account account = createAccount("test1@naver.com", true, "Tarun");

        refreshToken = createRefreshToken(account);
    }

    @Test
    public void login() throws Exception {
        SignInRequest request = new SignInRequest("test1@naver.com", "asdf123@");

        mvc.perform(post("/auth")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    public void login_400() throws Exception {
        SignInRequest request = new SignInRequest("test1@naver.com", "asdf15@");

        mvc.perform(post("/auth")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void login_404() throws Exception {
        SignInRequest request = new SignInRequest("test1@naver.com", "asdf1255@");

        mvc.perform(post("/auth")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void tokenRefresh() throws Exception {
        mvc.perform(patch("/auth")
                .header("X-Refresh-Token",refreshToken.getRefreshToken())
        ).andExpect(status().isNoContent());
    }

    @Test
    public void tokenRefresh_401() throws Exception {
        mvc.perform(patch("/auth")
                .header("X-Refresh-Token","asdf.asdf.asdf")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void test() {
        assertNotNull(refreshToken.getRefreshToken());
        assertNotNull(refreshToken.getRefreshExp());
        assertNotNull(refreshToken.getAccountId());
    }

}
