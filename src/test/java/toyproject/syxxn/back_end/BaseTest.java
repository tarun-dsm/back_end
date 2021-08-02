package toyproject.syxxn.back_end;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.pet.PetImage;
import toyproject.syxxn.back_end.entity.pet.PetImageRepository;
import toyproject.syxxn.back_end.entity.pet.PetInfo;
import toyproject.syxxn.back_end.entity.pet.PetInfoRepository;
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
import java.time.LocalDate;

@Component
public class BaseTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AccountRepository accountRepository;

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

    public void deleteEvery() {
        accountRepository.deleteAll();
        applicationRepository.deleteAll();
        postRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        reviewRepository.deleteAll();
        verifyNumberRepository.deleteAll();
    }

    public MockMvc setMvc() {
        return MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    public Account createAccount(String email, boolean isLocationConfirm, String nickname) {
        return accountRepository.save(
                Account.builder()
                        .email(email)
                        .password(encoder.encode("asdf123@"))
                        .sex(Sex.MALE)
                        .nickname(nickname)
                        .age(18)
                        .isExperienceRasingPet(false)
                        .experience(null)
                        .latitude(BigDecimal.ZERO)
                        .longitude(BigDecimal.ZERO)
                        .isLocationConfirm(isLocationConfirm)
                        .build()
        );
    }

    public Account createAccount(String email, boolean isLocationConfirm) {
        return accountRepository.save(
                Account.builder()
                        .email(email)
                        .password(encoder.encode("asdf123@"))
                        .sex(Sex.FEMALE)
                        .nickname("nickname")
                        .age(18)
                        .isExperienceRasingPet(true)
                        .experience("지금 또로랑 같이 산지 6년째입니당")
                        .latitude(BigDecimal.ZERO)
                        .longitude(BigDecimal.ZERO)
                        .isLocationConfirm(isLocationConfirm)
                        .build()
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
                        .account(account2)
                        .isAccepted(isAccepted)
                        .build()
        );
    }

    public Post createPost(Account account, boolean isEnd, String endDate) {
        Post post = postRepository.save(
                Post.builder()
                        .account(account)
                        .title("제목을 까먹었지 뭐야..")
                        .applicationEndDate(LocalDate.parse(endDate))
                        .protectionStartDate(LocalDate.of(2021,10,29))
                        .protectionEndDate(LocalDate.of(2021,10,30))
                        .contactInfo("010-0000-0000")
                        .description("랄랄라")
                        .isUpdated(false)
                        .isApplicationEnd(isEnd)
                        .build()
        );
        petInfoRepository.save(
                PetInfo.builder()
                        .petSex(Sex.MALE)
                        .petName("아몰랑고양이")
                        .petSpecies("코리안숏헤어인줄알았죠")
                        .post(post)
                        .build()
        );
        petImageRepository.save(
                PetImage.builder()
                        .path("https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMDA5MDhfMjI3%2FMDAxNTk5NTE0MTg2ODc2.XR5pv2EMxtHWqPvmQiKzdDehiQx_ocJmGYeQdg__1wgg.pws149iHO28YsC3jXUc25tJwkGPB8Cfzu7NjrOF2YxEg.JPEG.byb0111%2F1599514185873.jpg&type=a340")
                        .post(post)
                        .build()
        );
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

}
