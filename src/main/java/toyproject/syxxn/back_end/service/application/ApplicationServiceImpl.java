package toyproject.syxxn.back_end.service.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.exception.PostNotFoundException;
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
    public void protectionApplication(Long id) {
        Account account = accountRepository.findById(authenticationFacade.getUserId())
                .orElseThrow(UserNotAccessibleException::new);

        applicationRepository.findByAccountId(account.getId())
                .ifPresent(applicationRepository::delete);

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        applicationRepository.save(
               Application.builder()
                       .account(account)
                       .post(post)
                       .build()
        );
    }

}
