package toyproject.syxxn.back_end;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.syxxn.back_end.dto.request.CoordinatesRequest;
import toyproject.syxxn.back_end.dto.request.SignUpRequest;
import toyproject.syxxn.back_end.entity.account.Account;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackEndApplication.class)
@ActiveProfiles("test")
public class AccountControllerTest extends BaseTest{

    private MockMvc mvc;

    Account account;

    @BeforeEach
    public void setUp() {
        mvc = setMvc();

        account = createAccount("adsf1234@naver.com", true, "Tarun");
        createAccount("test1234@gmail.com", false, "ggosunnae");
        createVerifyNumber();
    }

    @AfterEach
    public void deleteAll() {
        deleteEvery();
    }

    @Test
    public void confirm_email() throws Exception {
        mvc.perform(get("/account/email/2011413lsy@dsm.hs.kr")
        ).andExpect(status().isOk());
    }

    @Test
    public void confirm_email_400() throws Exception {
        mvc.perform(get("/account/email/2011413lsy")
        ).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void confirm_email_409() throws Exception {
        mvc.perform(get("/account/email/adsf1234@naver.com")
        ).andExpect(status().isConflict()).andDo(print());
    }

    @Test
    public void confirm_nickname_english() throws Exception {
        mvc.perform(get("/account/nickname/asdfqwertf")
                .characterEncoding("UTF-8")
        ).andExpect(status().isOk());
    }

    @Test
    public void confirm_nickname_number() throws Exception {
        mvc.perform(get("/account/nickname/1234567890")
                .characterEncoding("UTF-8")
        ).andExpect(status().isOk());
    }

    @Test
    public void confirm_nickname_korean() throws Exception {
        mvc.perform(get("/account/nickname/내맘대로할거야글자수")
                .characterEncoding("UTF-8")
        ).andExpect(status().isOk());
    }

    @Test
    public void confirm_nickname_fail1() throws Exception {
        mvc.perform(get("/account/nickname/띄어쓰기도 가능함")
                .characterEncoding("UTF-8")
        ).andExpect(status().isOk());
    }

    @Test
    public void confirm_nickname_400() throws Exception {
        mvc.perform(get("/account/nickname/띄")
                .characterEncoding("UTF-8")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void confirm_nickname_409() throws Exception {
        mvc.perform(get("/account/nickname/Tarun")
                .characterEncoding("UTF-8")
        ).andExpect(status().isConflict());
    }

    @Test
    public void sign_up() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .email("2000ls@gmail.com")
                .password("passpa123@")
                .nickname("닉네임야호")
                .age(120)
                .sex("FEMALE")
                .isExperienceRasingPet(true)
                .experience("야호")
                .build();

        mvc.perform(post("/account")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isCreated());
    }

    @Test
    public void sign_up_400_1() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .email("2000ls")
                .password("passpa123@")
                .nickname("닉네임야호")
                .age(120)
                .sex("FEMALE")
                .isExperienceRasingPet(true)
                .experience("야호")
                .build();

        mvc.perform(post("/account")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void sign_up_401() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .email("2000ls@naver.com")
                .password("passpa123@")
                .nickname("닉네임이다 이거야")
                .age(120)
                .sex("FEMALE")
                .isExperienceRasingPet(true)
                .experience("야호")
                .build();

        mvc.perform(post("/account")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "adsf1234@naver.com", password = "asdf123@")
    @Test
    public void saveCoordinate() throws Exception {
        CoordinatesRequest request = new CoordinatesRequest(BigDecimal.valueOf(12.123456), BigDecimal.valueOf(32.12345));

        mvc.perform(put("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
        ).andExpect(status().isNoContent());

        assertTrue(account.getIsLocationConfirm());
    }

    @WithMockUser(value = "23@naver.com", password = "asdf123@")
    @Test
    public void saveCoordinate_404() throws Exception {
        CoordinatesRequest request = new CoordinatesRequest(BigDecimal.valueOf(12.123456), BigDecimal.valueOf(32.12345));

        mvc.perform(put("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void accountTest() {
        assertEquals(18, (int) account.getAge());
        assertFalse(account.getIsExperienceRasingPet());
        assertNotNull(account.getSex());
        assertNull(account.getExperience());
        assertNull(account.getPosts());
        assertNull(account.getApplications());
        assertNull(account.getWrittenReview());
        assertNull(account.getReviews());
        assertSame(account.getLatitude(), BigDecimal.ZERO);
        assertSame(account.getLongitude(), BigDecimal.ZERO);
    }

}
