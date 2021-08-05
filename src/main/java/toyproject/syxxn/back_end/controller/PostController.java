package toyproject.syxxn.back_end.controller;

import io.lettuce.core.ScriptOutputType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.dto.response.PostDetailsResponse;
import toyproject.syxxn.back_end.dto.response.PostResponse;
import toyproject.syxxn.back_end.service.post.PostService;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/post")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Integer postId) {
        postService.deletePost(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer writePost(@RequestBody PostRequest request) {
        return postService.writePost(request);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveFiles(@PathVariable("id") Integer postId,
            @RequestParam("files") List<MultipartFile> files) {
        postService.saveFiles(postId, files);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Integer updatePost(@PathVariable("id") Integer postId,
                             @RequestBody @Valid PostRequest request) {
        return postService.updatePost(postId, request);
    }

    @GetMapping("/{id}")
    public PostDetailsResponse getPostDetails(@PathVariable("id") Integer postId) {
        return postService.getPostDetails(postId);
    }

    @GetMapping
    public PostResponse getPosts() {
        return postService.getPosts();
    }

}
