package toyproject.syxxn.back_end.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.dto.response.CommentsResponse;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.comment.Comment;
import toyproject.syxxn.back_end.entity.comment.CommentRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.exception.CommentNotFoundException;
import toyproject.syxxn.back_end.exception.UserNotAccessibleException;
import toyproject.syxxn.back_end.service.util.AuthenticationUtil;
import toyproject.syxxn.back_end.service.util.PostUtil;
import toyproject.syxxn.back_end.service.util.UserUtil;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final AuthenticationUtil authenticationFacade;
    private final PostUtil postUtil;
    private final UserUtil userUtil;

    public void writeComment(String comment, Integer postId) {
        Account account = userUtil.getLocalConfirmAccount();
        Post post = postUtil.getPost(postId);

        commentRepository.save(
                Comment.builder()
                        .comment(comment)
                        .post(post)
                        .account(account)
                        .build()
        );
    }

    public void deleteComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> CommentNotFoundException.EXCEPTION);

        if(equalsEmail(comment.getWriter().getEmail())) {
            commentRepository.delete(comment);
        } else {
            throw new UserNotAccessibleException();
        }
    }

    public void updateComment(Integer commentId, String comment) {
        Comment c = commentRepository.findById(commentId)
                .filter(com -> com.getWriter().getEmail().equals(authenticationFacade.getUserEmail()))
                .orElseThrow(() -> CommentNotFoundException.EXCEPTION);

        if(equalsEmail(c.getWriter().getEmail())) {
            commentRepository.save(c.updateComment(comment));
        } else {
            throw new UserNotAccessibleException();
        }
    }

    public CommentsResponse getComments(Integer postId) {
        String email = userUtil.getLocalConfirmAccount().getEmail();
        Post post = postUtil.getPost(postId);

        return new CommentsResponse(
                commentRepository.findAllByPostOrderByCreatedAtDesc(post).stream().map(
                        c -> CommentsResponse.CommentDto.builder()
                                .id(c.getId())
                                .nickname(c.getWriter().getNickname())
                                .comment(c.getComment())
                                .createdAt(c.getCreatedAt() == null ? null : c.getCreatedAtToString())
                                .isUpdated(c.getIsUpdated())
                                .updatedAt(c.getUpdatedAt() == null ? null : c.getUpdatedAtToString())
                                .isMine(c.getWriter().getEmail().equals(email))
                            .build()
                ).collect(Collectors.toList())
        );
    }

    private boolean equalsEmail(String email) {
        return email.equals(authenticationFacade.getUserEmail());
    }
}
