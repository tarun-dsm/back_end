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
    public void protectionApplication(Long id) {
        Account account = accountRepository.findById(authenticationFacade.getUserId())
                    .orElseThrow(UserNotAccessibleException::new);

        applicationRepository.findByAccount(account)
                .ifPresent(application -> applicationRepository.deleteById(application.getId()));// 이 친구가 실행이 안돼요 ㅠㅠㅠ
        applicationRepository.deleteAll();

        System.out.println("깔깔깔ㄲ깔깔 껄껄껄껄");
        applicationRepository.flush();


        applicationRepository.findAll()
                .forEach(System.out::println);
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        System.out.println("여기까진 살아 있을텐데");
        System.out.println("죽지 마 친구야!");
        applicationRepository.save(
               Application.builder()
                       .account(account)
                       .post(post)
                       .build()
        );
        System.out.println("안돼 죽었어 ㅠㅠㅠ");
    }

}
