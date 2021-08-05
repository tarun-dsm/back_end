package toyproject.syxxn.back_end.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import toyproject.syxxn.back_end.dto.request.PetDto;
import toyproject.syxxn.back_end.dto.request.PostDto;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.dto.response.*;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.pet.PetImage;
import toyproject.syxxn.back_end.entity.pet.PetImageRepository;
import toyproject.syxxn.back_end.entity.pet.PetInfo;
import toyproject.syxxn.back_end.entity.pet.PetInfoRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.service.BaseService;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PetInfoRepository petInfoRepository;
    private final PetImageRepository  petImageRepository;

    private final BaseService baseService;

    @Value("${file.path}")
    private String path;

    @Override
    public void deletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        if (!baseService.getLocalConfirmAccount().getEmail().equals(post.getAccount().getEmail())) {
            throw new UserNotAccessibleException();
        }
        postRepository.delete(post);
    }

    @Override
    public void saveFiles(Integer postId, List<MultipartFile> files) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        for (PetImage petImage : post.getPetImages()) {
            File file = new File(petImage.getPath());
            file.delete();
            petImageRepository.delete(petImage);
        }

        saveFile(files, post);
    }

    @Transactional
    @Override
    public Integer writePost(PostRequest request) {
        Account account = baseService.getLocalConfirmAccount();
        PostDto postDto = request.getPost();
        PetDto petDto = request.getPet();
        String startDate = postDto.getProtectionStartDate();
        String endDate = postDto.getProtectionEndDate();

        startDateAfterEndDate(startDate, endDate);
        Post post = postRepository.save(
                Post.builder()
                        .title(postDto.getTitle())
                        .description(postDto.getDescription())
                        .account(account)
                        .protectionStartDate(LocalDate.parse(startDate))
                        .protectionEndDate(LocalDate.parse(endDate))
                        .applicationEndDate(LocalDate.parse(postDto.getApplicationEndDate()))
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

        return post.getId();
    }

    @Override
    public Integer updatePost(Integer postId, PostRequest request) {
        Account account = baseService.getLocalConfirmAccount();
        Post post = postRepository.findById(postId)
                .filter(p -> p.getAccount().getId().equals(account.getId()))
                .filter(p -> !p.getIsApplicationEnd())
                .orElseThrow(PostNotFoundException::new);

        String startDate = request.getPost().getProtectionStartDate();
        String endDate = request.getPost().getProtectionEndDate();

        startDateAfterEndDate(startDate, endDate);

        post.update(request.getPost());
        post.getPetInfo().update(request.getPet());
        postRepository.save(post);
        petInfoRepository.save(post.getPetInfo());

        return post.getId();
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
                .writerId(post.getAccount().getId())
                .nickname(post.getAccount().getNickname())
                .pet(petDetailsDto)
                .post(postDetailsDto)
                .build();
    }

    @Override
    public PostResponse getPosts() {
        Account account = baseService.getAccount();
        List<Post> posts = postRepository.findAllByIsApplicationEndFalseOrderByCreatedAtDesc();

        return new PostResponse(posts.stream()
                .filter(post -> account.getIsLocationConfirm() || distance(account, post.getAccount()) >= 1)
                .map(post -> PostResponseDto.builder()
                        .id(post.getId())
                        .writer(post.getAccount().getNickname())
                        .title(post.getTitle())
                        .firstImagePath(post.getPetImages().get(0).getPath())
                        .createdAt(post.getCreatedAt())
                        .build()).collect(Collectors.toList())
        );
    }

    private void startDateAfterEndDate(String startDate, String endDate) {
        if (LocalDate.parse(startDate).isAfter(LocalDate.parse(endDate))) {
            throw new InvalidScheduleSettingException();
        }
    }

    private void saveFile(List<MultipartFile> files, Post post) {
        try {
            if (files.size() > 5) {
                throw new FileNumberExceedException();
            }

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
        } catch (Exception e) {
            postRepository.delete(post);
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
