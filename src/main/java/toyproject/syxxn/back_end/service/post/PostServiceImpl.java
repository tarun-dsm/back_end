package toyproject.syxxn.back_end.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.pet.PetImageRepository;
import toyproject.syxxn.back_end.entity.pet.PetInfoRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.exception.PostNotFoundException;
import toyproject.syxxn.back_end.exception.UserNotAccessibleException;
import toyproject.syxxn.back_end.exception.UserNotUnauthenticatedException;
import toyproject.syxxn.back_end.security.auth.AuthenticationFacade;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final PetInfoRepository petInfoRepository;
    private final PetImageRepository  petImageRepository;

    private final AuthenticationFacade authenticationFacade;

    @Override
    public void deletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        if (authenticationFacade.getUserEmail().equals(getAccount().getEmail())) {
            throw new UserNotAccessibleException();
        }

        postRepository.delete(post);
    }

    private Account getAccount() {
        return accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .filter(Account::getIsLocationConfirm)
                .orElseThrow(UserNotUnauthenticatedException::new);
    }

}
