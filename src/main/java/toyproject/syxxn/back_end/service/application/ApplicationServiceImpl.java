package toyproject.syxxn.back_end.service.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.syxxn.back_end.dto.response.ApplicationDto;
import toyproject.syxxn.back_end.dto.response.ApplicationResponse;
import toyproject.syxxn.back_end.dto.response.MyApplicationDto;
import toyproject.syxxn.back_end.dto.response.MyApplicationResponse;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.security.auth.AuthenticationFacade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final ApplicationRepository applicationRepository;

    private final AuthenticationFacade authenticationFacade;

    @Override
    @Transactional
    public void protectionApplication(Integer postId) {
        Account account = getAccount();
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        if (account.getId().equals(post.getAccount().getId())) {
            throw new UserIsWriterException();
        }
        if (isApplicationClosed(post)) {
            throw new AfterApplicationClosedException();
        }

        applicationRepository.findByAccount(account)
                .filter(application -> application.getPost().getId().equals(post.getId()))
                .ifPresent(application -> {
                    throw new UserAlreadyApplicationException();
        });

        applicationRepository.save(
               Application.builder()
                       .account(account)
                       .post(post)
                       .isAccepted(false)
                       .build()
        );

    }

    @Override
    public void cancelApplication(Integer applicationId) {
        Account account = getAccount();

        Application application = applicationRepository.findById(applicationId)
                .filter(a -> a.getAccount().getId().equals(account.getId()))
                .orElseThrow(ApplicationNotFoundException::new);

        if (isApplicationClosed(application.getPost())) {
            throw new AfterApplicationClosedException();
        }

        applicationRepository.delete(application);
    }

    @Transactional
    @Override
    public void acceptApplication(Integer applicationId) {
        Account account = getAccount();

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(ApplicationNotFoundException::new);

        Post post = application.getPost();

        if (!post.getAccount().getId().equals(account.getId())) {
            throw new UserNotAccessibleException();
        }

        if (isApplicationClosed(post)) {
            throw new AfterApplicationClosedException();
        }

        application.acceptApplication();
        post.isEnd();
        applicationRepository.save(application);
        postRepository.save(post);
    }

    @Override
    public MyApplicationResponse getMyApplications() {
        Account account = getAccount();
        List<Application> applications = applicationRepository.findAllByAccount(account);
        List<MyApplicationDto> myApplications = new ArrayList<>();

        for (Application application: applications) {
            myApplications.add(
                    MyApplicationDto.builder()
                            .id(application.getId())
                            .applicationDate(application.getCreatedAt())
                            .isAccepted(application.getIsAccepted())
                            .postId(application.getPost().getId())
                            .postName(application.getPost().getTitle())
                            .isEnd(application.getPost().getIsEnd())
                            .build()
            );
        }

        return new MyApplicationResponse(myApplications);
    }

    @Override
    public ApplicationResponse getApplications(Integer postId) {
        Account account = getAccount();
        Post post = postRepository.findById(postId)
                .filter(p -> p.getAccount().getId().equals(account.getId()))
                .filter(p -> !p.getIsEnd())
                .orElseThrow(PostNotFoundException::new);

        List<Application> applications = applicationRepository.findAllByPost(post);
        List<ApplicationDto> applicationList = new ArrayList<>();

        for (Application application: applications) {
            applicationList.add(
                    ApplicationDto.builder()
                            .applicationId(application.getId())
                            .applicationDate(application.getCreatedAt())
                            .applicantId(application.getAccount().getId())
                            .applicantNickname(application.getAccount().getNickname())
                            .build()
            );
        }

        return new ApplicationResponse(applicationList);
    }

    private Account getAccount() {
        return accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .filter(Account::getIsLocationConfirm)
                .orElseThrow(UserNotAccessibleException::new);
    }

    private boolean isApplicationClosed(Post post) {
        return (LocalDate.now().isAfter(post.getApplicationEndDate()) || post.getIsEnd());
    }

}
