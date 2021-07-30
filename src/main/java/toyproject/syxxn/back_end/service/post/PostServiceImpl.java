package toyproject.syxxn.back_end.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import toyproject.syxxn.back_end.dto.request.PetDto;
import toyproject.syxxn.back_end.dto.request.PostDto;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.pet.PetImage;
import toyproject.syxxn.back_end.entity.pet.PetImageRepository;
import toyproject.syxxn.back_end.entity.pet.PetInfo;
import toyproject.syxxn.back_end.entity.pet.PetInfoRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.security.auth.AuthenticationFacade;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final PetInfoRepository petInfoRepository;
    private final PetImageRepository  petImageRepository;

    private final AuthenticationFacade authenticationFacade;

    @Value("${file.path}")
    private String path;

    @Override
    public void deletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        if (authenticationFacade.getUserEmail().equals(getAccount().getEmail())) {
            throw new UserNotAccessibleException();
        }
        postRepository.delete(post);
    }

    @Transactional
    @Override
    public Integer writePost(List<MultipartFile> files, PostRequest request) {
        Account account = getAccount();
        PostDto postDto = request.getPost();
        PetDto petDto = request.getPet();

        if (files.size() > 5) {
            throw new FileNumberExceedException();
        }

        Post post = postRepository.save(
                Post.builder()
                        .title(postDto.getTitle())
                        .description(postDto.getDescription())
                        .account(account)
                        .protectionStartDate(postDto.getProtectionStartDate())
                        .protectionEndDate(postDto.getProtectionEndDate())
                        .applicationEndDate(postDto.getApplicationEndDate())
                        .isApplicationEnd(false)
                        .isUpdated(false)
                        .contactInfo(postDto.getContactInfo())
                        .build()
        );

        petInfoRepository.save(
                PetInfo.builder()
                        .petName(petDto.getPetName())
                        .petSpecies(petDto.getPetSpecies())
                        .petSex(Sex.valueOf(petDto.getPetSex()))
                        .post(post)
                        .build()
        );

        saveFile(files, post);

        return post.getId();
    }

    private Account getAccount() {
        return accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .filter(Account::getIsLocationConfirm)
                .orElseThrow(UserNotUnauthenticatedException::new);
    }

    private void saveFile(List<MultipartFile> files, Post post) {
        try {
            for (MultipartFile file : files) {
                if (file.getOriginalFilename() == null) {
                    throw new FileNotFoundException();
                }
                String filePath = path + file.getOriginalFilename();

                String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                if (!(extension.contains("jpg") || extension.contains("jpeg") || extension.contains("png"))) {
                    throw new InvalidFileTypeException();
                }

                file.transferTo(new File(filePath));
                petImageRepository.save(
                        PetImage.builder()
                                .post(post)
                                .path(filePath)
                                .build()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileSaveFailedException();
        }
    }

}
