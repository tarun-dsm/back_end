package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.dto.response.PostDetailsResponse;
import toyproject.syxxn.back_end.dto.response.PostIdResponse;
import toyproject.syxxn.back_end.dto.response.PostResponse;
import toyproject.syxxn.back_end.service.post.PostService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable("id") Integer postId) {
        postService.deletePost(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostIdResponse writePost(@RequestBody @Valid PostRequest request) {
        return new PostIdResponse(postService.writePost(request));
    }

    @PostMapping("/post/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveFiles(@PathVariable("id") Integer postId,
            @RequestParam List<MultipartFile> files) {
        postService.saveFiles(postId, files);
    }

    @PatchMapping("/post/{id}")
    public PostIdResponse updatePost(@PathVariable("id") Integer postId,
                             @RequestBody @Valid PostRequest request) {
        return new PostIdResponse(postService.updatePost(postId, request));
    }

    @GetMapping("/post/{id}")
    public PostDetailsResponse getPostDetails(@PathVariable("id") Integer postId) {
        return postService.getPostDetails(postId);
    }

    @GetMapping("/posts")
    public PostResponse getPosts() {
        return postService.getPosts();
    }

}
