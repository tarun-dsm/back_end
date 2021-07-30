package toyproject.syxxn.back_end.service.post;

import org.springframework.web.multipart.MultipartFile;
import toyproject.syxxn.back_end.dto.request.PostRequest;

import java.util.List;

public interface PostService {
    void deletePost(Integer postId);
    Integer writePost(List<MultipartFile> files, PostRequest request);
    Integer updatePost(Integer postId, List<MultipartFile> files, PostRequest request);
}
