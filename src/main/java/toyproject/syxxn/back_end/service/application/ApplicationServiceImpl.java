package toyproject.syxxn.back_end.service.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.syxxn.back_end.dto.response.ApplicationDto;
import toyproject.syxxn.back_end.dto.response.ApplicationResponse;
import toyproject.syxxn.back_end.dto.response.MyApplicationDto;
import toyproject.syxxn.back_end.dto.response.MyApplicationResponse;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.application.ApplicationCustomRepository;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.service.facade.PostUtil;
import toyproject.syxxn.back_end.service.facade.UserUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationCustomRepository applicationCustomRepository;
    private final PostRepository postRepository;

    private final PostUtil postUtil;
    private final UserUtil baseService;

    @Override
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
                        .account(account)
                        .post(post)
                        .build()
        );

    }

    @Override
    public void cancelApplication(Integer applicationId) {
        Account account = baseService.getLocalConfirmAccount();

        Application application = applicationRepository.findById(applicationId)
                .filter(a -> a.getAccount().equals(account))
                .orElseThrow(ApplicationNotFoundException::new);

        if (!isApplicationClosed(application.getPost())) {
            applicationRepository.delete(application);
        }
    }

    @Transactional
    @Override
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
    }

    @Override
    public MyApplicationResponse getMyApplications() {
        Account account = baseService.getLocalConfirmAccount();
        List<Application> applications = applicationRepository.findAllByAccount(account);

        return new MyApplicationResponse(applications.stream().map(
                application -> MyApplicationDto.builder()
                        .id(application.getId())
                        .applicationDate(application.getCreatedAt())
                        .isAccepted(application.getIsAccepted())
                        .postId(application.getPost().getId())
                        .postName(application.getPost().getTitle())
                        .isEnd(application.getPost().getIsApplicationEnd())
                        .build()
        ).collect(Collectors.toList()));
    }

    @Override
    public ApplicationResponse getApplications(Integer postId) {
        Account account = baseService.getLocalConfirmAccount();
        Post post = postUtil.getPost(postId, account);

        List<Application> applications = applicationRepository.findAllByPost(post);

        return new ApplicationResponse(applications.stream().map(application ->
                ApplicationDto.builder()
                        .applicationId(application.getId())
                        .applicationDate(application.getCreatedAt())
                        .applicantId(application.getAccount().getId())
                        .isAccepted(application.getIsAccepted())
                        .applicantNickname(application.getAccount().getNickname())
                        .administrationDivision(application.getAccount().getAdministrationDivision())
                        .build()
        ).collect(Collectors.toList()));
    }

    private boolean isApplicationClosed(Post post) {
        if (LocalDate.now().isAfter(post.getApplicationEndDate()) || post.getIsApplicationEnd()) {
            throw new AfterApplicationClosedException();
        } else return false;
    }

}
