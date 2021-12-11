package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import toyproject.syxxn.back_end.dto.request.CommentRequest;
import toyproject.syxxn.back_end.dto.response.CommentsResponse;
import toyproject.syxxn.back_end.service.comment.CommentService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/comment/{id}")
    public void writeComment(@PathVariable("id") Integer postId,
                             @RequestBody @Valid CommentRequest request) {
        commentService.writeComment(request.getComment(), postId);
    }

    @DeleteMapping("/comment/{id}")
    public void deleteComment(@PathVariable("id") Integer commentId) {
        commentService.deleteComment(commentId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/comment/{id}")
    public void updateComment(@PathVariable("id") Integer commentId,
                              @RequestBody @Valid CommentRequest request) {
        commentService.updateComment(commentId, request.getComment());
    }

    @GetMapping("/comments/post/{id}")
    public CommentsResponse getCommentsForPost(@PathVariable("id") Integer postId) {
        return commentService.getCommentsForPost(postId);
    }

}
