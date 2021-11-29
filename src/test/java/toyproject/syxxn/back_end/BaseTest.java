package toyproject.syxxn.back_end;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.pet.*;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshToken;
import toyproject.syxxn.back_end.entity.refreshtoken.RefreshTokenRepository;
import toyproject.syxxn.back_end.entity.review.Review;
import toyproject.syxxn.back_end.entity.review.ReviewRepository;
import toyproject.syxxn.back_end.entity.verify.VerifyNumber;
import toyproject.syxxn.back_end.entity.verify.VerifyNumberRepository;
import toyproject.syxxn.back_end.security.jwt.JwtTokenProvider;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackEndApplication.class)
@ActiveProfiles("test")
public class BaseTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PetInfoRepository petInfoRepository;

    @Autowired
    private PetImageRepository petImageRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private VerifyNumberRepository verifyNumberRepository;

    @Autowired
    private PasswordEncoder encoder;

    @AfterEach
    public void deleteEvery() {
        accountRepository.deleteAll();
        applicationRepository.deleteAll();
        postRepository.deleteAll();
        petInfoRepository.deleteAll();
        petImageRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        reviewRepository.deleteAll();
        verifyNumberRepository.deleteAll();
    }

    public MockMvc setMvc() {
        return MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    public Account createAccount(String email, String nickname, boolean isLocationConfirm) {
        return accountRepository.save(
                new Account(email, encoder.encode("asdf123@"), nickname, isLocationConfirm)
        );
    }

    public RefreshToken createRefreshToken(Account account) {
        return refreshTokenRepository.save(
                RefreshToken.builder()
                        .accountId(account.getId())
                        .refreshExp(12134L)
                        .refreshToken(jwtTokenProvider.generateRefreshToken(account.getId()))
                        .build()
        );
    }

    public VerifyNumber createVerifyNumber() {
        return verifyNumberRepository.save(
                new VerifyNumber("2000ls@gmail.com", "123456", true)
        );
    }

    public Application createApplication(Account account1, Account account2, boolean isEnd, boolean isAccepted, String endDate) {
        return applicationRepository.save(
                Application.builder()
                        .post(createPost(account1, isEnd, endDate))
                        .applicant(account2)
                        .isAccepted(isAccepted)
                        .build()
        );
    }

    public Application createApplication(Account account1, Account account2, boolean isAccepted) {
        return applicationRepository.save(
                Application.builder()
                        .post(createPost(account1, false, "2021-09-08"))
                        .applicant(account2)
                        .isAccepted(isAccepted)
                        .build()
        );
    }

    public Post createPost(Account account, boolean isEnd, String endDate) {
        Post post = postRepository.save(new Post(createPostRequest(endDate), account, isEnd));
        petInfoRepository.save(post.getPetInfo());
        petImageRepository.save(new PetImage(post, "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMDA5MDhfMjI3%2FMDAxNTk5NTE0MTg2ODc2.XR5pv2EMxtHWqPvmQiKzdDehiQx_ocJmGYeQdg__1wgg.pws149iHO28YsC3jXUc25tJwkGPB8Cfzu7NjrOF2YxEg.JPEG.byb0111%2F1599514185873.jpg&type=a340"));
        return post;
    }

    public Review createReview(Account account1, Account account2, BigDecimal grade) {
        return reviewRepository.save(
                Review.builder()
                        .target(account1)
                        .writer(account2)
                        .application(createApplication(account1, account2, true, true, "2021-05-22"))
                        .grade(grade)
                        .comment("대체 왜 ratingScore 안에는 값이 안들어가는건지 모르겠음 어이없어")
                        .build()
        );
    }

    public PostRequest createPostRequest() {
        return PostRequest.builder()
                .title("이것은 제목")
                .description("이것은 설명")
                .protectionStartDate("2021-08-05")
                .protectionEndDate("2021-08-06")
                .applicationEndDate("2021-08-04")
                .contactInfo("010-0000-0000")
                .petName("또로")
                .petSpecies("코리안숏헤어")
                .petSex("MALE")
                .animalType("AMPHIBIANS")
                .build();
    }

    public PostRequest createPostRequest(String endDate) {
        return PostRequest.builder()
                .title("이것은 제목")
                .description("이것은 설명")
                .protectionStartDate("2021-08-05")
                .protectionEndDate("2021-08-06")
                .applicationEndDate(endDate)
                .contactInfo("010-0000-0000")
                .petName("또로")
                .petSpecies("코리안숏헤어")
                .petSex("MALE")
                .animalType("AMPHIBIANS")
                .build();
    }

}
