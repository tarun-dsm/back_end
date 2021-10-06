package toyproject.syxxn.back_end.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.dto.response.CommentDto;
import toyproject.syxxn.back_end.dto.response.CommentsResponse;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.comment.Comment;
import toyproject.syxxn.back_end.entity.comment.CommentRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.exception.CommentNotFoundException;
import toyproject.syxxn.back_end.exception.PostNotFoundException;
import toyproject.syxxn.back_end.exception.UserNotAccessibleException;
import toyproject.syxxn.back_end.service.facade.AuthenticationFacade;
import toyproject.syxxn.back_end.service.facade.BaseService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final AuthenticationFacade authenticationFacade;
    private final BaseService baseService;

    @Override
    public void writeComment(String comment, Integer postId) {
        Account account = baseService.getLocalConfirmAccount();
        Post post = postRepository.findById(postId)
                .filter(p -> !p.getIsApplicationEnd())
                .orElseThrow(PostNotFoundException::new);

        commentRepository.save(
                Comment.builder()
                        .comment(comment)
                        .post(post)
                        .account(account)
                        .build()
        );
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if(comment.getWriter().getEmail().equals(authenticationFacade.getUserEmail())) {
            commentRepository.delete(comment);
        } else {
            throw new UserNotAccessibleException();
        }
    }

    @Override
    public void updateComment(Integer commentId, String comment) {
        Comment c = commentRepository.findById(commentId)
                .filter(com -> com.getWriter().getEmail().equals(authenticationFacade.getUserEmail()))
                .orElseThrow(CommentNotFoundException::new);

        if(c.getWriter().getEmail().equals(authenticationFacade.getUserEmail())) {
            commentRepository.save(c.updateComment(comment));
        } else {
            throw new UserNotAccessibleException();
        }
    }

    @Override
    public CommentsResponse getComments(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return new CommentsResponse(
                commentRepository.findAllByPostOrderByCreatedAtDesc(post).stream().map(
                        c -> CommentDto.builder()
                                .id(c.getId())
                                .nickname(c.getWriter().getNickname())
                                .comment(c.getComment())
                                .createdAt(c.getCreatedAt() == null ? null : c.getCreatedAt().toString())
                                .isUpdated(c.getIsUpdated())
                                .updatedAt(c.getUpdatedAt() == null ? null : c.getUpdatedAt().toString())
                            .build()
                ).collect(Collectors.toList())
        );
    }
}
