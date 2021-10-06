package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.syxxn.back_end.dto.request.CommentRequest;
import toyproject.syxxn.back_end.dto.response.CommentsResponse;
import toyproject.syxxn.back_end.service.comment.CommentService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/comment")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{id}")
    public void writeComment(@PathVariable("id") Integer postId,
                             @RequestBody @Valid CommentRequest request) {
        commentService.writeComment(request.getComment(), postId);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable("id") Integer commentId) {
        commentService.deleteComment(commentId);
    }

    @PatchMapping("/{id}")
    public void updateComment(@PathVariable("id") Integer commentId,
                              @RequestBody @Valid CommentRequest request) {
        commentService.updateComment(commentId, request.getComment());
    }

    public CommentsResponse getComments(Integer postId) {
        return commentService.getComments(postId);
    }

}
