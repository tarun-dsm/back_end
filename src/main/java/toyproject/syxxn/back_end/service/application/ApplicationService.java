package toyproject.syxxn.back_end.service.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.syxxn.back_end.dto.response.ApplicationResponse;
import toyproject.syxxn.back_end.dto.response.MyApplicationResponse;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.application.ApplicationCustomRepository;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.entity.review.ReviewRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.service.util.EmailUtil;
import toyproject.syxxn.back_end.service.util.PostUtil;
import toyproject.syxxn.back_end.service.util.S3Util;
import toyproject.syxxn.back_end.service.util.UserUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationCustomRepository applicationCustomRepository;
    private final PostRepository postRepository;
    private final ReviewRepository reviewRepository;

    private final EmailUtil emailUtil;
    private final PostUtil postUtil;
    private final S3Util s3Util;
    private final UserUtil userUtil;

    private static final String NEW_APPLICATION = "회원님의 게시글에 새로운 신청이 있습니다.";
    private static final String ACCEPT_APPLICATION = "회원님의 신청이 수락되었습니다.";

    @Async
    @Transactional
    public void protectionApplication(Integer postId) {
        Account account = userUtil.getLocalConfirmAccount();
        Post post = postUtil.getPost(postId);

        if (account.getEmail().equals(post.getAccount().getEmail())) {
            throw UserIsWriterException.EXCEPTION;
        } if (isApplicationClosed(post)) {
            throw AfterApplicationClosedException.EXCEPTION;
        } if (applicationCustomRepository.existsNotEndApplication(account)) {
            throw UserAlreadyApplicationException.EXCEPTION;
        }

        applicationRepository.save(
                Application.builder()
                        .isAccepted(false)
                        .applicant(account)
                        .post(post)
                        .build()
        );

        try {
            String text = "\'" + post.getTitle() + "\' 게시글에 " + account.getNickname() + "님이 신청하셨습니다.";
            emailUtil.sendEmail(post.getAccount().getEmail(), NEW_APPLICATION, text);
        } catch (Exception e) {
            throw EmailSendException.EXCEPTION;
        }

    }

    public void cancelApplication(Integer postId) {
        String email = userUtil.getLocalConfirmAccount().getEmail();

        Application application = applicationRepository.findByPostIdAndApplicantEmail(postId, email)
                .orElseThrow(() -> ApplicationNotFoundException.EXCEPTION);

        if (!isApplicationClosed(application.getPost())) {
            applicationRepository.delete(application);
        }
    }

    @Async
    @Transactional
    public void acceptApplication(Integer applicationId) {
        Account account = userUtil.getLocalConfirmAccount();
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> ApplicationNotFoundException.EXCEPTION);
        Post post = application.getPost();

        if (!post.getAccount().equals(account)) {
            throw UserNotAccessibleException.EXCEPTION;
        }

        if (!isApplicationClosed(post)) {
            application.acceptApplication();
            post.isEnd();
            applicationRepository.save(application);
            postRepository.save(post);
        }

        try {
            String text = "\'" + post.getTitle() + "\' 게시글의 신청이 수락되었습니다.";
            emailUtil.sendEmail(application.getApplicant().getEmail(), ACCEPT_APPLICATION, text);
        } catch (Exception e) {
            throw EmailSendException.EXCEPTION;
        }
    }

    public MyApplicationResponse getMyApplications() {
        Account me = userUtil.getLocalConfirmAccount();
        return new MyApplicationResponse(me.getApplications().stream().map(
                application -> {
                    Post post = application.getPost();
                    return MyApplicationResponse.MyApplicationDto.builder()
                            .id(application.getId())
                            .isAccepted(application.getIsAccepted())
                            .postId(post.getId())
                            .postName(post.getTitle())
                            .startDate(post.getProtectionStartDate().toString())
                            .endDate(post.getProtectionEndDate().toString())
                            .firstImagePath(s3Util.getS3ObjectUrl(post.getFirstImagePath()))
                            .administrationDivision(post.getAccount().getAdministrationDivision())
                            .isEnd(post.getIsApplicationEnd())
                            .isWrittenReview(reviewRepository.findByWriterAndApplication(me, application).isPresent())
                            .build();
                }
        ).collect(Collectors.toList()));
    }

    public ApplicationResponse getApplicationsForPost(Integer postId) {
        Account account = userUtil.getLocalConfirmAccount();
        Post post = postUtil.getPost(postId, account);
        List<Application> applications = applicationRepository.findAllByPost(post);

        return new ApplicationResponse(applications.stream().map(application ->
                ApplicationResponse.ApplicationDto.builder()
                        .applicationId(application.getId())
                        .applicationDate(application.getCreatedAtToString())
                        .applicantId(application.getApplicant().getId())
                        .isAccepted(application.getIsAccepted())
                        .applicantNickname(application.getApplicant().getNickname())
                        .administrationDivision(application.getApplicant().getAdministrationDivision())
                        .isWrittenReview(reviewRepository.findByWriterAndApplication(account, application).isPresent())
                        .build()
        ).collect(Collectors.toList()));
    }

    private boolean isApplicationClosed(Post post) {
        if (LocalDate.now().isAfter(post.getApplicationEndDate()) || post.getIsApplicationEnd()) {
            throw AfterApplicationClosedException.EXCEPTION;
        } else return false;
    }

}
