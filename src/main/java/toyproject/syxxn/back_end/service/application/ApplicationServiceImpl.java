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
import toyproject.syxxn.back_end.exception.PostNotFoundException;
import toyproject.syxxn.back_end.exception.UserAlreadyApplicationException;
import toyproject.syxxn.back_end.exception.UserIsWriterException;
import toyproject.syxxn.back_end.exception.UserNotAccessibleException;
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
    public void protectionApplication(Integer id) {
        Account account = accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .filter(Account::getIsLocationConfirm)
                .orElseThrow(UserNotAccessibleException::new);
        Post post = postRepository.findById(id)
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

}
