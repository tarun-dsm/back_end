package toyproject.syxxn.back_end.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import toyproject.syxxn.back_end.dto.request.PetDto;
import toyproject.syxxn.back_end.dto.request.PostDto;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.dto.response.PetDetailsDto;
import toyproject.syxxn.back_end.dto.response.PostDetailsDto;
import toyproject.syxxn.back_end.dto.response.PostDetailsResponse;
import toyproject.syxxn.back_end.dto.response.PostResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Integer updatePost(Integer postId, List<MultipartFile> files, PostRequest request) {
        Account account = getAccount();
        Post post = postRepository.findById(postId)
                .filter(p -> p.getAccount().getId().equals(account.getId()))
                .orElseThrow(PostNotFoundException::new);

        post.update(request.getPost());
        post.getPetInfo().update(request.getPet());
        postRepository.save(post);
        petInfoRepository.save(post.getPetInfo());

        petImageRepository.deleteAllByPost(post);
        saveFile(files, post);

        return null;
    }

    @Override
    public PostDetailsResponse getPostDetails(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        PetInfo petInfo = post.getPetInfo();

        List<PetImage> petImages = petImageRepository.findAllByPost(post);
        List<String> filePaths = new ArrayList<>();

        for (PetImage petImage : petImages) {
            filePaths.add(petImage.getPath());
        }

        PostDetailsDto postDetailsDto = PostDetailsDto.builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .protectionStartDate(post.getProtectionStartDate())
                .protectionEndDate(post.getProtectionEndDate())
                .applicationEndDate(post.getApplicationEndDate())
                .contactInfo(post.getContactInfo())
                .createdAt(post.getCreatedAt())
                .isApplicationEnd(post.getIsApplicationEnd())
                .isUpdated(post.getIsUpdated())
                .build();

        PetDetailsDto petDetailsDto = PetDetailsDto.builder()
                .petName(petInfo.getPetName())
                .petSpecies(petInfo.getPetSpecies())
                .petSex(petInfo.getPetSex().toString())
                .filePaths(filePaths)
                .build();


        return PostDetailsResponse.builder()
                .nickname(post.getAccount().getNickname())
                .pet(petDetailsDto)
                .post(postDetailsDto)
                .build();
    }

    @Override
    public PostResponse getPosts() {
        Account account = getAccount();
        List<Post> posts = postRepository.findAllByIsApplicationEndFalseOrderByCreatedAtDesc();

        return new PostResponse(posts.stream()
                .filter(post -> account.getIsLocationConfirm() || distance(account, post.getAccount()) >= 1)
                .map(post -> toyproject.syxxn.back_end.dto.response.PostDto.builder()
                        .id(post.getId())
                        .writer(post.getAccount().getNickname())
                        .title(post.getTitle())
                        .firstImagePath(post.getPetImages().get(0).getPath())
                        .createdAt(post.getCreatedAt())
                        .build()).collect(Collectors.toList())
        );
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

    private static double distance(Account account, Account writer) {
        double writerLon = writer.getLongitude().doubleValue();
        double writerLat = writer.getLatitude().doubleValue();
        double myLon = account.getLongitude().doubleValue();
        double myLat = account.getLatitude().doubleValue();

        double theta = Math.abs(writerLon - myLon);
        double distance = Math.sin(degreeToRadion(myLat)) * Math.sin(degreeToRadion(writerLat)) + Math.cos(degreeToRadion(myLat)) * Math.cos(degreeToRadion(writerLat)) * Math.cos(degreeToRadion(theta));

        distance = Math.acos(distance);
        distance = radionToDegrees(distance);
        distance = distance * 60 * 1.1515 * 1.609344;

        return distance;
    }

    private static double degreeToRadion(double deg) {
        return (deg * Math.PI / 180);
    }

    private static double radionToDegrees(double rad) {
        return (rad * 180 / Math.PI);
    }

}
