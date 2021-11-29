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
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.service.util.EmailUtil;
import toyproject.syxxn.back_end.service.util.PostUtil;
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

    private final EmailUtil emailUtil;
    private final PostUtil postUtil;
    private final UserUtil baseService;

    private static final String NEW_APPLICATION = "회원님의 게시글에 새로운 신청이 있습니다.";
    private static final String ACCEPT_APPLICATION = "회원님의 신청이 수락되었습니다.";

    @Async
    @Transactional
    public void protectionApplication(Integer postId) {
        Account account = baseService.getLocalConfirmAccount();
        Post post = postUtil.getPost(postId);

        if (account.getEmail().equals(post.getAccount().getEmail())) {
            throw new UserIsWriterException();
        } if (isApplicationClosed(post)) {
            throw new AfterApplicationClosedException();
        } if (applicationCustomRepository.existsNotEndApplication(account)) {
            throw new UserAlreadyApplicationException();
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
            throw new EmailSendException();
        }

    }

    public void cancelApplication(Integer postId) {
        String email = baseService.getLocalConfirmAccount().getEmail();

        Application application = applicationRepository.findByPostIdAndApplicantEmail(postId, email)
                .orElseThrow(ApplicationNotFoundException::new);

        if (!isApplicationClosed(application.getPost())) {
            applicationRepository.delete(application);
        }
    }

    @Async
    @Transactional
    public void acceptApplication(Integer applicationId) {
        Account account = baseService.getLocalConfirmAccount();
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(ApplicationNotFoundException::new);
        Post post = application.getPost();

        if (!post.getAccount().equals(account)) {
            throw new UserNotAccessibleException();
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
            throw new EmailSendException();
        }
    }

    public MyApplicationResponse getMyApplications() {
        List<Application> applications = applicationRepository.findAllByApplicant(baseService.getLocalConfirmAccount());

        return new MyApplicationResponse(applications.stream().map(
                application -> MyApplicationResponse.MyApplicationDto.builder()
                        .id(application.getId())
                        .applicationDate(application.getCreatedAtToString())
                        .isAccepted(application.getIsAccepted())
                        .postId(application.getPost().getId())
                        .postName(application.getPost().getTitle())
                        .isEnd(application.getPost().getIsApplicationEnd())
                        .build()
        ).collect(Collectors.toList()));
    }

    public ApplicationResponse getApplications(Integer postId) {
        Post post = postUtil.getPost(postId, baseService.getLocalConfirmAccount());
        List<Application> applications = applicationRepository.findAllByPost(post);

        return new ApplicationResponse(applications.stream().map(application ->
                ApplicationResponse.ApplicationDto.builder()
                        .applicationId(application.getId())
                        .applicationDate(application.getCreatedAtToString())
                        .applicantId(application.getApplicant().getId())
                        .isAccepted(application.getIsAccepted())
                        .applicantNickname(application.getApplicant().getNickname())
                        .administrationDivision(application.getApplicant().getAdministrationDivision())
                        .build()
        ).collect(Collectors.toList()));
    }

    private boolean isApplicationClosed(Post post) {
        if (LocalDate.now().isAfter(post.getApplicationEndDate()) || post.getIsApplicationEnd()) {
            throw new AfterApplicationClosedException();
        } else return false;
    }

}
