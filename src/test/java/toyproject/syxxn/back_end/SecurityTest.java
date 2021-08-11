package toyproject.syxxn.back_end;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.exception.InvalidTokenException;
import toyproject.syxxn.back_end.exception.UserNotFoundException;
import toyproject.syxxn.back_end.security.auth.AuthDetails;
import toyproject.syxxn.back_end.security.auth.AuthDetailsService;
import toyproject.syxxn.back_end.service.facade.AuthenticationFacade;
import toyproject.syxxn.back_end.security.jwt.JwtTokenProvider;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityTest extends BaseTest {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthDetailsService authDetailsService;

    Account account;
    String refreshToken;

    @BeforeEach
    public void setUp() {
        account = createAccount("test1@naver.com", false, "Tarun");
        refreshToken = createRefreshToken(account).getRefreshToken();
    }

    @AfterEach
    public void deleteAll() {
        deleteEvery();
    }

    @WithMockUser(value = "test@gmail.com", password = "testpass")
    @Test
    public void getUserEmail() {
        assertEquals(authenticationFacade.getUserEmail(), "test@gmail.com");
    }

    @Test
    public void generatedToken() {
        assertNotNull(jwtTokenProvider.generateAccessToken(account.getId()));
        assertNotNull(jwtTokenProvider.generateRefreshToken(account.getId()));
    }

    @Test
    public void isRefreshToken() {
        assertTrue(jwtTokenProvider.isRefreshToken(refreshToken));
        assertThrows(InvalidTokenException.class, () -> jwtTokenProvider.isRefreshToken("asdf.asdf.asdf"));
    }

    @Test
    public void loadUserByUsername() {
        assertNotNull(authDetailsService.loadUserByUsername(account.getId().toString()));
        assertThrows(UserNotFoundException.class, () -> authDetailsService.loadUserByUsername("99999"));
    }

    @WithMockUser(value = "test1@naver.com", password = "asdf123@")
    @Test
    public void authDetailsTest() {
        AuthDetails authDetails = new AuthDetails(account);
        assertTrue(authDetails.isEnabled());
        assertTrue(authDetails.isAccountNonExpired());
        assertTrue(authDetails.isAccountNonLocked());
        assertTrue(authDetails.isCredentialsNonExpired());
        assertNotNull(authDetails.getUsername());
        assertNotNull(authDetails.getPassword());
        assertNotNull(authDetails.getAuthorities());
    }

}
