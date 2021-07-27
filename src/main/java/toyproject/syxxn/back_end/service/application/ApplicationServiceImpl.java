package toyproject.syxxn.back_end.service.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.security.auth.AuthenticationFacade;

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
        Account account = accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .filter(Account::getIsLocationConfirm)
                .orElseThrow(UserNotAccessibleException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        if (account.getId().equals(post.getAccount().getId())) {
            throw new UserIsWriterException();
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
        Account account = accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotAccessibleException::new);

        Application application = applicationRepository.findById(applicationId)
                .filter(a -> a.getAccount().getId().equals(account.getId()))
                .orElseThrow(ApplicationNotFoundException::new);

        applicationRepository.delete(application);
    }

    @Override
    public void acceptApplication(Integer applicationId) {

    }

}
