package toyproject.syxxn.back_end;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import toyproject.syxxn.back_end.dto.request.PetDto;
import toyproject.syxxn.back_end.dto.request.PostDto;
import toyproject.syxxn.back_end.dto.request.PostRequest;
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
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackEndApplication.class)
@ActiveProfiles("test")
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
        return postRepository.save(
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
                .build());
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
        return new PostRequest(
                PostDto.builder()
                        .title("이것은 제목")
                        .description("이것은 설명")
                        .protectionStartDate("2021-08-06")
                        .protectionEndDate("2021-08-09")
                        .applicationEndDate("2021-08-04")
                        .contactInfo("010-0000-0000")
                        .build(),
                PetDto.builder()
                        .petName("또로")
                        .petSpecies("코리안숏헤어")
                        .petSex(Sex.MALE.toString())
                        .build()
        );
    }

    public PetInfo createPetInfo(Account account) {
        Post post = createPost(account, false, "2021-08-16");

        petImageRepository.save(
                PetImage.builder()
                        .path("https://dimg.donga.com/ugc/CDB/WEEKLY/Article/5b/b3/22/85/5bb32285000ed2738de6.jpg")
                        .post(post)
                        .build()
        );

        return petInfoRepository.save(
                PetInfo.builder()
                        .petSex(Sex.MALE)
                        .petName("아몰랑고양이")
                        .petSpecies("코리안숏헤어인줄알았죠")
                        .post(post)
                        .build()
        );
    }

}
