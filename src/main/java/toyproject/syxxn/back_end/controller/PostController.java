package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.service.post.PostService;

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
    public Integer writePost(@RequestParam("files") List<MultipartFile> files,
                             @RequestBody PostRequest request) {
        return postService.writePost(files, request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Integer updatePost(@PathVariable("id") Integer postId,
                              @RequestParam("files") List<MultipartFile> files,
                             @RequestBody PostRequest request) {
        return postService.updatePost(postId, files, request);
    }

}
