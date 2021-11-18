package toyproject.syxxn.back_end.service.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import toyproject.syxxn.back_end.entity.pet.PetImage;
import toyproject.syxxn.back_end.entity.pet.PetImageRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.service.util.PostUtil;
import toyproject.syxxn.back_end.service.util.S3Util;
import toyproject.syxxn.back_end.service.util.UserUtil;

import java.io.*;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FileService {

    private final PetImageRepository petImageRepository;

    private final PostUtil postUtil;
    private final UserUtil userUtil;
    private final S3Util s3Util;

    public void saveFiles(Integer postId, List<MultipartFile> files) {
        Post post = postUtil.getPost(postId, userUtil.getLocalConfirmAccount());

        if (files.size() > 5) {
            throw new FileNumberExceedException();
        }

        for (PetImage petImage : post.getPetImages()) {
            File file = new File(petImage.getPath());
            if (file.exists() && file.delete())
                s3Util.delete(petImage.getPath());
        }

        try {
            for (MultipartFile file : files) {
                petImageRepository.save(
                        new PetImage(post, s3Util.uploadImage(file)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileSaveFailedException();
        }
    }



}
