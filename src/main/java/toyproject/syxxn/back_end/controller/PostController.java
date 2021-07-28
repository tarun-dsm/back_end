package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toyproject.syxxn.back_end.service.post.PostService;

@RequestMapping("/post")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Integer postId) {
        postService.deletePost(postId);
    }

}
