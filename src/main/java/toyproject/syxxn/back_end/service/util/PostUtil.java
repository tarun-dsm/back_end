package toyproject.syxxn.back_end.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.exception.PostNotFoundException;
import toyproject.syxxn.back_end.exception.UserNotAccessibleException;

@RequiredArgsConstructor
@Component
public class PostUtil {

    private final PostRepository postRepository;

    public Post getPost(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
    }

    public Post getNotApplicationEndPost(Integer id) {
        return postRepository.findById(id)
                .filter(p -> !p.getIsApplicationEnd())
                .orElseThrow(PostNotFoundException::new);
    }

    public Post getPost(Integer id, Account account) {
        Post post =  postRepository.findById(id)
                .filter(Post::getIsApplicationEnd)
                .orElseThrow(PostNotFoundException::new);

        if (!postIsMine(account, post))
            throw new UserNotAccessibleException();

        return post;
    }

    public boolean postIsMine(Account account, Post post) {
        return post.getAccount().getEmail().equals(account.getEmail());
    }

}
