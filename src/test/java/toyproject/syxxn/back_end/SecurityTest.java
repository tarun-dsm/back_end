package toyproject.syxxn.back_end;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshToken;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshTokenRepository;
import toyproject.syxxn.back_end.exception.InvalidTokenException;
import toyproject.syxxn.back_end.exception.UserNotFoundException;
import toyproject.syxxn.back_end.security.auth.AuthDetails;
import toyproject.syxxn.back_end.security.auth.AuthDetailsService;
import toyproject.syxxn.back_end.security.auth.AuthenticationFacade;
import toyproject.syxxn.back_end.security.jwt.JwtTokenProvider;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackEndApplication.class)
@ActiveProfiles("test")
public class SecurityTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AuthDetailsService authDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    Account account;
    String refreshToken;

    @BeforeEach
    public void setUp() {
        account = accountRepository.save(
                Account.builder()
                        .email("test1@naver.com")
                        .password(encoder.encode("asdf123@"))
                        .sex(Sex.MALE)
                        .nickname("나는야1번")
                        .age(18)
                        .isExperienceRasingPet(false)
                        .experience(null)
                        .address("경기도 서울시 구성동")
                        .isLocationConfirm(true)
                        .build()
        );

        refreshToken = refreshTokenRepository.save(
                RefreshToken.builder()
                        .accountId(account.getId())
                        .refreshExp(12134L)
                        .refreshToken(jwtTokenProvider.generateRefreshToken(account.getId()))
                        .build()
        ).getRefreshToken();
    }

    @AfterEach
    public void deleteAll() {
        accountRepository.deleteAll();
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
