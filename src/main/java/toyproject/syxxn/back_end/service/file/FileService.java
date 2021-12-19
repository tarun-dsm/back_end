package toyproject.syxxn.back_end.service.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import toyproject.syxxn.back_end.entity.pet.PetImage;
import toyproject.syxxn.back_end.entity.pet.PetImageRepository;
import toyproject.syxxn.back_end.entity.pet.PetInfoRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.service.util.PostUtil;
import toyproject.syxxn.back_end.service.util.S3Util;
import toyproject.syxxn.back_end.service.util.UserUtil;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FileService {

    private final PostRepository postRepository;
    private final PetImageRepository petImageRepository;
    private final PetInfoRepository petInfoRepository;

    private final PostUtil postUtil;
    private final UserUtil userUtil;
    private final S3Util s3Util;

    @Transactional
    public void saveFiles(Integer postId, List<MultipartFile> files) {
        Post post = postUtil.getPost(postId, userUtil.getLocalConfirmAccount());

        if (files.size() > 5) {
            throw FileNumberExceedException.EXCEPTION;
        }
        deleteImage(post.getPetImages());

        try {
            for (MultipartFile file : files) {
                petImageRepository.save(
                        new PetImage(post, s3Util.uploadImage(file)));
            }
            saveFirstImagePath(post);
        } catch (IOException e) {
            petInfoRepository.delete(post.getPetInfo());
            postRepository.delete(post);
            deleteImage(post.getPetImages());
            throw FileSaveFailedException.EXCEPTION;
        }
    }

    private void deleteImage(List<PetImage> petImages) {
        for (PetImage petImage : petImages) {
            s3Util.delete(petImage.getSavedPath());
            petImageRepository.delete(petImage);
        }
    }

    private void saveFirstImagePath(Post post) {
        PetImage petImage = petImageRepository.findAllByPost(post).get(0);
        post.setFirstImagePath(petImage != null ? petImage.getSavedPath() : null);

        postRepository.save(post);
    }

}
