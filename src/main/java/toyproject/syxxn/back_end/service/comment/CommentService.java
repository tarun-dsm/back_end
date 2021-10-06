package toyproject.syxxn.back_end.service.comment;

import toyproject.syxxn.back_end.dto.response.CommentsResponse;

public interface CommentService {
    void writeComment(String comment, Integer postId);
    void deleteComment(Integer commentId);
    void updateComment(Integer commentId, String comment);
    CommentsResponse getComments(Integer postId);
}
